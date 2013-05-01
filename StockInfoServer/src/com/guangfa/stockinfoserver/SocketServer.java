package com.guangfa.stockinfoserver;

import org.apache.log4j.Logger;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-30
 * Time: 下午7:31
 * To change this template use File | Settings | File Templates.
 */
public class SocketServer implements Runnable {
    private final Logger logger = Logger.getLogger(SocketServer.class);
    private int port;

    public SocketServer(int port) {
        this.port = port;
    }

    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket s = ss.accept();
                new Thread(new Servicer(s)).start();
            }
        } catch (BindException exception) {
            logger.error("the listenPort[" + this.port + "] already in use!", exception);
            System.exit(0);
        } catch (Exception e) {
            logger.error("create ServerSocket occur error:", e);
            System.exit(0);
        }
    }
}
