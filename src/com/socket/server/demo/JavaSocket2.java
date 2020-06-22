package com.socket.server.demo;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * SocketServer服务器开发
 * @author Cherry
 * 2020年5月7日
 */
public class JavaSocket2 {
    public static void main(String[] args) {
        //默认HTTP请求
        while(true) {
            redirector();
            singleFile();
        }
    }
    
    //重定向服务器
    public static void redirector() {
        //重定向
        String url = "http://www.baidu.com";
        try(ServerSocket server = new ServerSocket(8080);
                Socket socket = server.accept();){
            Writer out = new OutputStreamWriter(socket.getOutputStream(),"UTF-8");
            StringBuilder sb = new StringBuilder();
            sb.append("HTTP/1.0 302 FOUND\r\n");
            sb.append("Date:" + new Date() + "\r\n");
            sb.append("Server: Redirector 1.1\r\n");
            sb.append("Location:" +url+ "\r\n");
            sb.append("Content-type:text/html\r\n");
            String header = sb.toString();
            out.write(header);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //单文件服务器
    public static void singleFile() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.0 200 OK\r\n");
        sb.append("Server:singleFile 2.0\r\n");
        sb.append("Content-length:");
        sb.append(100);
        sb.append("Content-type:text/*;;charset=utf-8\r\n");
        String header = sb.toString();
        
        try(ServerSocket server = new ServerSocket(8081);
            Socket socket = server.accept();){
            byte[] data = Files.readAllBytes(Paths.get("./ts.bat"));
            Writer out = new OutputStreamWriter(socket.getOutputStream(),"UTF-8");
            out.write(header);
            String content = new String(data);
            out.write(content);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
