package com.steve.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpParser {
    private final static Logger logger = LoggerFactory.getLogger(HttpParser.class);

    // SPACE, CARRIAGE RETURN, LINE FEED in HEXADECIMAL
    private static final int SPACE = 0x20; //SPACE - 32
    private static final int CARRIAGE_RETURN = 0x0D; // CARRIAGE RETURN - 13
    private static final int LINE_FEED = 0x0A; // LINE FEED -10

    /**
     * design choice to not make it static as we can instantiate a new parser for each connection, giving choice to modify if required
     */
    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(reader, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            parseHeaders(reader, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseBody(reader, request);
        return request;
    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();
        boolean crlfFound = false;

        int _byte;
        while ((_byte = reader.read()) >=0) {
            if (_byte == CARRIAGE_RETURN) {
                _byte = reader.read();
                if (_byte == LINE_FEED) {
                    if (!crlfFound) {
                        crlfFound = true;

                        // Do Things like processing
                        processSingleHeaderField(processingDataBuffer, request);
                        // Clear the buffer
                        processingDataBuffer.delete(0, processingDataBuffer.length());
                    } else {
                        // Two CRLF received, end of Headers section
                        return;
                    }
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            } else {
                crlfFound = false;
                // Append to Buffer
                processingDataBuffer.append((char)_byte);
            }
        }
    }
    private void processSingleHeaderField(StringBuilder processingDataBuffer, HttpRequest request) throws HttpParsingException {
        String rawHeaderField = processingDataBuffer.toString();
        Pattern pattern = Pattern.compile("^(?<fieldName>[!#$%&’*+\\-./^_‘|˜\\dA-Za-z]+):\\s?(?<fieldValue>[!#$%&’*+\\-./^_‘|˜(),:;<=>?@[\\\\]{}\" \\dA-Za-z]+)\\s?$");

        Matcher matcher = pattern.matcher(rawHeaderField);
        if (matcher.matches()) {
            // We found a proper header
            String fieldName = matcher.group("fieldName");
            String fieldValue = matcher.group("fieldValue");
            request.addHeader(fieldName, fieldValue);
        } else{
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

// Read the first line of the request to know whether it is a GET, POST etc type message.
    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder processDataBuffer = new StringBuilder(); // to process bytes into string;

        boolean methodParsed = false; // a boolean value to know whether it is GET, POST type.
        boolean requestTargetParsed = false; // To Know the targeted version of the HTTP request
        int _byte; // holds the request.
        while ((_byte = reader.read()) >= 0) {
            if (_byte == CARRIAGE_RETURN) {
                _byte = reader.read();
                if (_byte == LINE_FEED) {
                    logger.debug("Request Version to Process : {}", processDataBuffer.toString());
                    if(!methodParsed || !requestTargetParsed){
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    try {
                        request.setHttpVersion(processDataBuffer.toString());
                    } catch (BadHttpVersionException e) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    return;
                }else{
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            if (_byte == SPACE) {
                // Process the previous data
                if (!methodParsed) {
                    logger.debug("Request Method to Process : {}", processDataBuffer.toString());
                    request.setMethod(processDataBuffer.toString());
                    methodParsed = true;
                } else if (!requestTargetParsed) {
                    logger.debug("Request target to process : {}", processDataBuffer.toString());
                    request.setRequestTarget(processDataBuffer.toString());
                    requestTargetParsed = true;
                } else{
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                // clear the buffer
                processDataBuffer.delete(0, processDataBuffer.length());
            } else {
                processDataBuffer.append((char) _byte);
                if(!methodParsed){
                    if(processDataBuffer.length() > HttpMethod.MAX_LENGTH){
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }
}
