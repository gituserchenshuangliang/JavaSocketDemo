package com.socket.thread.demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 请求处理线程
 * @author Cherry
 * 2020年5月7日
 */
public class RequestProcessorRunable implements Runnable {
    private final Logger logger = Logger.getLogger(RequestProcessorRunable.class.getName());
    private Socket socket;
    private File rootDir;
    private String indexFile = "index.html";
    private String page404 = "404.html";
    private String page500 = "500.html";
    
    public RequestProcessorRunable(Socket socket, File rootDir,String indexFile) {
        this.socket = socket;
        if(rootDir.isFile()) {
            throw new IllegalArgumentException("please put a directory , not a file!");
        }
        try {
            rootDir = rootDir.getCanonicalFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.rootDir = rootDir;
        if(indexFile != null) {
            this.indexFile = indexFile;
        }
    }

    @Override
    public void run() {
        //安全检查
        String root = rootDir.getPath();
        try {
            OutputStream os = new BufferedOutputStream(socket.getOutputStream());
            
            InputStream ins = new BufferedInputStream(socket.getInputStream());
            
            StringBuilder request = new StringBuilder(80);
            while(true) {
                int c = ins.read();
                if(c=='\r' || c == '\n' || c == -1) {
                    break;
                }
                request.append((char)c);
            }
            String get  = request.toString();
            logger.info(socket.getRemoteSocketAddress() + " " + get);
            String[] tokens = get.split("\\s+");
            String method = tokens[0];
            String version = "";
            if(method.contentEquals("GET")) {
                String fileName = tokens[1];
                if(fileName.endsWith("/")) {
                    fileName += indexFile;
                    }
                if(tokens.length > 2) {
                    version = tokens[2];
                }
                File theFile = new File(rootDir,fileName.substring(1,fileName.length()));
                String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName.substring(1,fileName.length()));
                //不让客户端超出文档目录之外
                if(theFile.canRead() && theFile.getCanonicalPath().startsWith(root)) {
                    byte[] theData = Files.readAllBytes(theFile.toPath());
                    if(version.startsWith("HTTP/")) {
                        sendHeader(os,"HTTP/1.0 200 OK\r\n",contentType,theData.length);
                    }
                    //发送文件，图片或其他音频，使用底层输出流
                    os.write(theData);
                    logger.info("发送文件：" + theFile.getPath());
                    os.flush();
                }else {//无法找到文件
                    String url404 = "/" + page404;
                    redirector(os, url404);
                }
            }else {//方法不等于GET
                String url500 = "/" + page500;
                redirector(os, url500);
            }
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.WARNING,"Error talking to " + socket.getRemoteSocketAddress(),e);
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
    
    private void sendHeader(OutputStream out, String code, String contentType,int length) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(code);
        sb.append("Date: " + new Date() + "\r\n");
        sb.append("Server： HTTPServer 2.0\r\n");
        sb.append("Content-length: " + length + "\r\n");
        sb.append("Conent-type:" + contentType + "\r\n\r\n");
        String header = sb.toString();
        out.write(header.getBytes("UTF-8"));
        out.flush();
    }
    
    //重定向
    public static void redirector(OutputStream out,String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.0 302 FOUND\r\n");
        sb.append("Date: " + new Date() + "\r\n");
        sb.append("Server: Redirector 1.1\r\n");
        sb.append("Content-type: text/html\r\n");
        sb.append("Location: " +url+ "\r\n\r\n");
        String header = sb.toString();
        out.write(header.getBytes("UTF-8"));
        out.flush();
    }
}
