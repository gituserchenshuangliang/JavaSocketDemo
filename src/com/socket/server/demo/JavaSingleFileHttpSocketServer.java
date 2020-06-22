package com.socket.server.demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * HTTP服务器：单文件处理服务器，无论什么请求都发送一个文件
 * @author Cherry
 * 2020年5月6日
 */
public class JavaSingleFileHttpSocketServer {
    //日志记录
    private static Logger logger = Logger.getLogger(JavaSingleFileHttpSocketServer.class.getName());
    private byte[] content;//内容
    private byte[] header;//头部
    private int port;//端口号
    private String encoding;//编码
    
    public JavaSingleFileHttpSocketServer(byte[] data,String encoding,String mimeType,int port) {
        this.content = data;
        this.encoding = encoding;
        this.port = port;
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.0 200 OK\r\n");
        sb.append("Server:oneFile 2.0\r\n");
        sb.append("Content-length:");
        sb.append(this.content.length);
        sb.append("Content-type:");
        sb.append(mimeType);
        sb.append(";charset=");
        sb.append(encoding);
        sb.append("\r\n\r\n");
        this.header = sb.toString().getBytes(Charset.forName("US-ASCII"));
    }
    
    public JavaSingleFileHttpSocketServer(String data,String encoding,String mimeType,int port) {
        this(data.getBytes(Charset.forName("US-ASCII")),encoding,mimeType,port);
    }
    
    //启动服务器
    public void start() {
        ExecutorService exe = Executors.newFixedThreadPool(500);
            
        try(ServerSocket server = new ServerSocket(this.port);){
            logger.info("Accepting connections on port "+server.getLocalPort());
            while(true) {
                    try{
                        Socket socket = server.accept();
                        exe.submit(() -> {
                            response(socket);
                        });
                    }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //服务器响应
    public void response(Socket socket) {
        try {
            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            InputStream in = new BufferedInputStream(socket.getInputStream());  
            StringBuilder sb = new StringBuilder();
            
            //读取第一行
            while(true) {
                int c = in.read();
                if(c == '\r' || c == '\n' || c == -1) {
                    break;
                }
                sb.append((char)c);
            }
            if(sb.toString().indexOf("HTTP/") != -1) {
                out.write(header);
            }
            out.write(content);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args) {
        //监听端口
        //args[0] = "./ts.bat";
        String file = "./ts.bat";
        String encoding = "UTF-8";
        int port = 8080;
        if(args.length > 1) {
            port  = Integer.parseInt(args[1]);
            if(port <1 || port >65535) {
                port = 80;
            }
        }
        if(args.length > 2) {
            encoding = args[2];
        }
        try {
            //文件数据
            byte[] data = Files.readAllBytes(Paths.get(file));
            String contentType = URLConnection.getFileNameMap().getContentTypeFor(file);
            //构建启动服务器
            new JavaSingleFileHttpSocketServer(data,encoding,contentType,port).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
