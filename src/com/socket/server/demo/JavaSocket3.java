package com.socket.server.demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaSocket3
 * @author Cherry
 * 2020年5月8日
 */
public class JavaSocket3 {
    private final static Logger logger = Logger.getLogger(JavaSocket3.class.getName());
   
    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    
    private byte[] header;
    
    private byte[] content;
    
    public JavaSocket3(byte[] header, byte[] content) {
        super();
        this.header = header;
        this.content = content;
    }
    
    public static void main(String[] args) throws IOException {
        //byte[] cf = contentFile("E:\\ServiceDemo\\404.html");
        byte[] cf = contentFile("./chars.txt");
        byte[] h2xx = header2XX(cf.length);
        new JavaSocket3(h2xx,cf).start();;
//        byte[] h3xx = header3XX("");
//        new JavaSocket3(h3xx,null).start();;
    }
    
    public void start() {
        try (ServerSocket server = new ServerSocket(8080);){
            logger.info("Socket running at " + server);
            while(true) {
                try{
                Socket socket  = server.accept();
                pool.submit(() -> {
                    response(socket,header,content);
                });
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void response(Socket socket,byte[] header,byte[] content) {
        try {
            InputStream in = new BufferedInputStream(socket.getInputStream());
            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            StringBuilder sb = new StringBuilder(80);
            while(true) {
                int c = in.read();
                if(c == '\r' || c == '\n' || c == -1) {
                    break;
                }
                sb.append((char)c);
            }
            String get = sb.toString();
            logger.info("request heaer: " + get);
            if(get.toString().indexOf("HTTP/") != -1) {
                out.write(header);
                if(content != null && !content.equals("")) {
                    out.write(content);
                }
                out.flush();
            }
        } catch (SocketException e) {
            logger.log(Level.WARNING,"链接异常：" + e);
        } catch (IOException e) {
            logger.log(Level.WARNING,"IO异常：" + e);
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static byte[] header3XX(String url) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.0 302 FOUND\r\n");
        sb.append("Date:" + new Date() + "\r\n");
        sb.append("Server: Redirector 1.1\r\n");
        sb.append("Location:" +url+ "\r\n");
        sb.append("Content-type:text/html\r\n\r\n");
        return sb.toString().getBytes("UTF-8");
    }
    
    public static byte[] header2XX(int length) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.0 200 OK\r\n");
        sb.append("Server:singleFile 2.0\r\n");
        sb.append("Content-length:");
        sb.append(length);
        sb.append("\r\n");
        sb.append("Content-type:*/*;;charset=utf-8\r\n\r\n");
        return sb.toString().getBytes("UTF-8");
    }
    
    public static byte[] contentFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
    
    public static byte[] contentTxt(String text) throws UnsupportedEncodingException {
        return text.getBytes("UTF-8");
    }
}
