package com.steve.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            logger.info("Opening I/O Stream");
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            String html = "<html><head><title>Java Server</title></head/><body><h1>This content is sent by the java server</h1></body>";
            /**
             * //Status Line : HTTP VERSION RESPONSE_CODE RESPONSE_MESSAGE
             * */
            final String CRLF = "\r\n";
            String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length:" + html.getBytes().length + CRLF + CRLF + html + CRLF + CRLF;

            outputStream.write(response.getBytes());

/**
 * Closing the sockets for simple exit.
 */

            logger.info("Connection Processing finished");
//            serverSocket.close();
        } catch (IOException e) {
            logger.error("Issue in communication.", e);
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
