package com.socket.client.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.Objects;

/**
 * JavaSocketClient
 * @author Cherry
 * 2020年5月5日
 */
public class JavaSocketClient {
    public static void main(String[] args) {
        socketClientThree();
    }
    
    public static void socketClientOne() {
        try (Socket socket = new Socket("192.168.144.1",123);){ 
            outToSend(socket, "to server`s message !");
            inToReceive(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void socketClientTwo() {
        try (Socket socket = new Socket("localhost",8080);){ 
            inToReceive(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void socketClientThree() {
        try (Socket socket = new Socket("192.168.0.102",8080);){ 
            String header = "OPTIONS /JavaeeShow/ HTTP/1.0\r\n" + "Host: 192.168.0.102:8080\r\n\r\n";
            outToSend(socket, header);
            inToReceive(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //将输入流转换成String
    public static String inToReceive(Socket s) {
        Objects.requireNonNull(s);
        try {
          InputStream in = s.getInputStream();
          Reader reads = new InputStreamReader(in,"utf-8");
          BufferedReader read = new BufferedReader(reads);
          StringBuilder sb = new StringBuilder();
          int cap = in.available();
          int i = 0;
          String str;
          while((str = read.readLine()) != null) {
              i++;
              if(i > cap) {
                  break;
              }
              sb.append(str);
          }
            String msg = sb.toString();
            show("Receive:"+msg);
            return msg;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    //将信息发送
    public static void outToSend(Socket s ,String msg) {
        Objects.requireNonNull(s);
        try {
            OutputStream out = s.getOutputStream();
            Writer write = new OutputStreamWriter(out);
            write.write(msg);
            write.flush();
            show("Send:"+msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //控制台输出对象
    public static void show(Object o) {
        System.out.println(o);
    }
}
