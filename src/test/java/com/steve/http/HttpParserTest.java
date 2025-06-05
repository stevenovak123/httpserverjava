package com.steve.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class HttpParserTest {

    private static HttpParser httpParser;


    @BeforeAll
    public static void beforeClass() {
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValidGETTestCase());
        } catch (HttpParsingException e) {
            fail(e);
        }
        assertEquals(HttpMethod.GET, request.getMethod());

    }

    @Test
    void parseBadHttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateInvalidGETTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }


    }

    @Test
    void parseInvalidMethodNameRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateInvalidMethodName());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }


    }

    @Test
    void parseInvalidNumberOfItems() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateInvalidNumberOfItems());
            fail();
        } catch (HttpParsingException e) {

            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }


    }
    @Test
    void parseEmptyRequestLine() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateEmptyRequestLine());
            fail();
        } catch (HttpParsingException e) {

            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }


    }
    @Test
    void parseOnlyCRnoLF() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateOnlyCRnoLF());
            fail();
        } catch (HttpParsingException e) {

            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }


    }


    private InputStream generateValidGETTestCase() {
        String rawData = "GET / HTTP/1.1\r\n" + "Host: localhost:8080\r\n" + "Connection: keep-alive\r\n" + "Cache-Control: max-age=0\r\n" + "sec-ch-ua: \"Chromium\";v=\"136\", \"Microsoft Edge\";v=\"136\", \"Not.A/Brand\";v=\"99\"\r\n" + "sec-ch-ua-mobile: ?0\r\n" + "sec-ch-ua-platform: \"Windows\"\r\n" + "Upgrade-Insecure-Requests: 1\r\n" + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36 Edg/136.0.0.0\r\n" + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" + "Sec-Fetch-Site: none\r\n" + "Sec-Fetch-Mode: navigate\r\n" + "Sec-Fetch-User: ?1\r\n" + "Sec-Fetch-Dest: document\r\n" + "Accept-Encoding: gzip, deflate, br, zstd\r\n" + "Accept-Language: en-US,en;q=0.9,en-IN;q=0.8\r\n" + "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    private InputStream generateInvalidGETTestCase() {
        String rawData = "GeT / HTTP/1.1\r\n" + "Host: localhost:8080\r\n" + "Connection: keep-alive\r\n" + "Cache-Control: max-age=0\r\n" + "sec-ch-ua: \"Chromium\";v=\"136\", \"Microsoft Edge\"";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    private InputStream generateInvalidMethodName() {
        String rawData = "GETTTTTTT / HTTP/1.1\r\n" + "Host: localhost:8080\r\n" + "Connection: keep-alive\r\n" + "Cache-Control: max-age=0\r\n" + "sec-ch-ua: \"Chromium\";v=\"136\", \"Microsoft Edge\"";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    // More than 3 items not allowed
    private InputStream generateInvalidNumberOfItems() {
        String rawData = "GET / ABCSWED TES HTTP/1.1\r\n" + "Host: localhost:8080\r\n" + "Connection: keep-alive\r\n" + "Cache-Control: max-age=0\r\n" + "sec-ch-ua: \"Chromium\";v=\"136\", \"Microsoft Edge\"";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    private InputStream generateEmptyRequestLine() {
        String rawData = "\r\n" + "Host: localhost:8080\r\n" + "Connection: keep-alive\r\n" + "Cache-Control: max-age=0\r\n" + "sec-ch-ua: \"Chromium\";v=\"136\", \"Microsoft Edge\"";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }
    private InputStream generateOnlyCRnoLF() {
        String rawData = "\r\n" + "Host: localhost:8080\r" + "Connection: keep-alive\r\n" + "Cache-Control: max-age=0\r\n" + "sec-ch-ua: \"Chromium\";v=\"136\", \"Microsoft Edge\"";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

}