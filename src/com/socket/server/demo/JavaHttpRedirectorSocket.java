package com.socket.server.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * HTTPSocket重定向服务器
 * @author Cherry
 * 2020年5月7日
 */
public class JavaHttpRedirectorSocket {
    private static final Logger logger = Logger.getLogger(JavaHttpRedirectorSocket.class.getName());
    private int port;
    private String newSite;
    
    public JavaHttpRedirectorSocket(int port,String newSite) {
        this.port = port;
        this.newSite = newSite;
    }
    
    public void start() {
        ExecutorService pool = Executors.newFixedThreadPool(50);
        try(ServerSocket server = new ServerSocket(port);){
            logger.info("Redirecting connections on port " + server.getLocalPort() + " to " + newSite);
            int i = 0;
            while(true) {
                Socket socket = server.accept();
                i++;
                logger.info("Socket: " + i);
                pool.submit(() ->{
                    response(socket);
                });
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        
    }
    
    public void response(Socket socket) {
        try {
            Reader in = new InputStreamReader(socket.getInputStream(),"UTF-8");
            Writer out = new OutputStreamWriter(socket.getOutputStream(),"UTF-8");
            StringBuilder sb = new StringBuilder(80);
            while(true) {
                int c = in.read();
                if(c == '\r' || c == '\n' || c == -1) {
                    break;
                }
                sb.append((char)c);
            }
            String get = sb.toString();
            String[] pieces = get.split("\\w*");
            String theFile = pieces[1];
            if(get.toString().indexOf("HTTP/") != -1) {
                out.write("HTTP/1.0 302 FOUND\r\n");
                out.write("Date:" + new Date() + "\r\n");
                out.write("Server: Redirector 1.1\r\n");
                out.write("Location:" + newSite + theFile + "\r\n");
                out.write("Content-type:text/html\r\n");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        int port = 8080;
        String newSite = "http://www.baidu.com";
        new JavaHttpRedirectorSocket(port, newSite).start();
    }

}
