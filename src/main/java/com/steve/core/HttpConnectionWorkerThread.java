package com.steve.core;

import com.steve.http.*;
import com.steve.io.ReadFileException;
import com.steve.io.WebRootHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private Socket socket;
    private WebRootHandler webRootHandler;
    private HttpParser httpParser = new HttpParser();

    public HttpConnectionWorkerThread(Socket socket, WebRootHandler webRootHandler) {
        this.socket = socket;
        this.webRootHandler = webRootHandler;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            logger.info("Opening I/O Stream");
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            HttpRequest request = httpParser.parseHttpRequest(inputStream);
            HttpResponse response = handleRequest(request);

            outputStream.write(response.getResponseBytes());

            logger.info("Connection Processing finished");

        } catch (IOException e) {
            logger.error("Issue in communication.", e);
        } catch (HttpParsingException ex) {
            logger.error("Bad Request", ex);

            HttpResponse response = new HttpResponse.Builder().httpVersion(HttpVersions.HTTP_1_1.LITERAL).statusCode(ex.getErrorCode()).build();
            try {
                outputStream.write(response.getResponseBytes());
            } catch (IOException e) {
                logger.error("Problem with communication", e);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Something wrong with finally inputStream case");
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("Something wrong with finally outputStream case");
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error("Something wrong with Socket Case");
                }
            }
        }
    }

    private HttpResponse handleRequest(HttpRequest request) {

        switch (request.getMethod()) {
            case GET:
                logger.info(" * GET Request");
                return handleGetRequest(request, true);
            case HEAD:
                logger.info(" * HEAD Request");
                return handleGetRequest(request, false);
            default:
                return new HttpResponse.Builder().httpVersion(request.getCompatibleVersion().LITERAL).statusCode(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED).build();
        }

    }

    private HttpResponse handleGetRequest(HttpRequest request, boolean setMessageBody) {
        try {

            HttpResponse.Builder builder = new HttpResponse.Builder().httpVersion(request.getCompatibleVersion().LITERAL).statusCode(HttpStatusCode.OK).addHeader(HttpHeaderName.CONTENT_TYPE.headerName, webRootHandler.getFileMimeType(request.getRequestTarget()));

            if (setMessageBody) {
                byte[] messageBody = webRootHandler.getFileByteArrayData(request.getRequestTarget());
                builder.addHeader(HttpHeaderName.CONTENT_LENGTH.headerName, String.valueOf(messageBody.length)).messageBody(messageBody);
            }

            return builder.build();

        } catch (FileNotFoundException e) {

            return new HttpResponse.Builder().httpVersion(request.getCompatibleVersion().LITERAL).statusCode(HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND).build();

        } catch (ReadFileException e) {

            return new HttpResponse.Builder().httpVersion(request.getCompatibleVersion().LITERAL).statusCode(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR).build();
        }

    }
}
