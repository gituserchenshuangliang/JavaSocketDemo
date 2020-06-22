package com.socket.server.demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.socket.thread.demo.GeneralSocketRunnable;

/**
 * JavaSocketServer
 * @author Cherry
 * 2020年5月5日
 */
public class JavaSocket {
    private final static Logger logger = Logger.getLogger(JavaSocket.class.getName());
    private static ExecutorService pool = Executors.newFixedThreadPool(10);
    public static void main(String[] args) {
        byte[] b = {(byte) 192,(byte) 168,(byte) 144,1};
        InetAddress addr = null;
        try {
            addr = InetAddress.getByAddress(b);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        try (ServerSocket server = new ServerSocket(8081,0,addr);){
            logger.info("Socket running at " + server);
            while(true) {
                try{
                Socket s  = server.accept();
                pool.submit(new GeneralSocketRunnable(s,"服务器反馈的信息！"));
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}