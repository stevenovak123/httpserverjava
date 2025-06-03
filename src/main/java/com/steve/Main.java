package com.steve;

import com.steve.config.Configuration;
import com.steve.config.ConfigurationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.cert.CRL;

// Driver  class
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from HTTP server!");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

        System.out.println("Using Port: " + configuration.getPort());
        System.out.println("Using webroot: " + configuration.getWebroot());

/**
 * Server Socket to create tcp connections
 */
        try {
            ServerSocket serverSocket = new ServerSocket(configuration.getPort());
            // Accept connections.
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

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
            inputStream.close();
            outputStream.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}