package com.steve.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(ServerListenerThread.class);

    private int port;
    private String webroot;
    private ServerSocket serverSocket;

    public ServerListenerThread(int port, String webroot) throws IOException {
        this.webroot = webroot;
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                // Accept connections.
                Socket socket = serverSocket.accept();
                logger.info("Connection accepted" + socket.getInetAddress());

//                Create a HTTPConnectionWorkerThread
                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start();
            }
        } catch (IOException e) {
            logger.error("Problem with sockets",e);
            throw new RuntimeException(e);
        }finally{
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {

                }
            }
        }
    }
}

