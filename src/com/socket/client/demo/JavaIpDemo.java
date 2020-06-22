package com.socket.client.demo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Enumeration;


/**
 * IP 
 * @author Cherry
 * 2020年5月4日
 */
public class JavaIpDemo {

    public static void main(String[] args) throws IOException {
        //inetAddressDemo();
        //networkInterrfaceDemo();
        //urlDemo();
        //urlDemo2();
        printURL("http://localhost:8080");
    }
    
    public static void printURL(String httpURL) throws IOException {
        URL u2 = new URL(httpURL);
        InputStream is2 = u2.openStream();
        inPrint(is2);
    }
    
    public static void inetAddressDemo() throws UnknownHostException {
        //指定IP域名
        InetAddress ia = InetAddress.getByName("www.baidu.com");
        show(ia.getHostAddress());
        show(ia.getHostName());
        show(ia);
        //判断IP地址类型
        show(ia.isMulticastAddress());
        try {
            show(ia.isReachable(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //按IP地址
        byte[] b = {14,(byte) 215,(byte) 177,38};
        InetAddress ins = InetAddress.getByAddress(b);
        show(ins);
        
        //本地IP
        InetAddress il = InetAddress.getLocalHost();
        show(il);
            
        //创建IPv4和IPv6对象;
        Inet4Address i4 = (Inet4Address) Inet4Address.getByName("www.bing.com");
        show(i4);
    }
    
    public static void networkInterrfaceDemo() throws UnknownHostException, SocketException {
        //InetAddress ia = InetAddress.getByName("www.baidu.com");
        //NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
        //列出主机上所有的网络接口
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        while(en.hasMoreElements()) {
            show(en.nextElement());
        }
    }
    
    //URL
    public static void urlDemo() throws IOException {
        //FTP协议
        String ftpURL = "ftp://192.168.144.1/ts.bat";
        URL u = new URL(ftpURL);
        show(u.getPath());
        show(u.getPort());
        show(u.getProtocol());
        show(u.getHost());
        show(u.getUserInfo());
        show(u.getQuery());
        
        InputStream is = u.openStream();
        inPrint(is);
        
        //HTTP协议
        String httpURL = "http://www.baidu.com";
        URL u2 = new URL(httpURL);
        
        InputStream is2 = u2.openStream();
        inPrint(is2);
        
        //URL构造函数
        //https://github.com/gituserchenshuangliang/SpringBootLearn/blob/master/pom.xml
        URL u3 = new URL("https","github.com","/gituserchenshuangliang/SpringBootLearn/blob/master/pom.xml");
        //application.properties替换pom.xml
        URL u4 = new URL(u3,"application.properties");
        InputStream is4 = u4.openStream();
        inPrint(is4);
        
        String url = "ftp://192.168.144.1/";
        URL u5 = new URL(url);
        URLConnection conn = u5.openConnection();
        InputStream is5 = conn.getInputStream();
        inPrint(is5);
        
        String code = URLEncoder.encode("https://github.com/gituserchenshuangliang/SpringBootLearn/blob/master/pom.xml","utf-8");
        show(code);
       String decode = URLDecoder.decode(code,"utf-8");
       show(decode);;
    }
    
    /*
     * URLConnection content-type content-length content-encoding date
     * last-modified expires
     */
    public static void urlDemo2() {
        String url = "http://localhost:8080/say?say=Cherry";
        try {
            URL u = new URL(url);
            URLConnection connection = u.openConnection();
            //show(connection.getContentType());
            //show(connection.getContentEncoding());
            //show(connection.getContentLength());
            for (int i = 0; i < 6; i++) {
                //show(i + "-->" + connection.getHeaderFieldKey(i)+":"+ connection.getHeaderField(i));
            }
            HttpURLConnection http = (HttpURLConnection) connection;
            http.setRequestMethod("GET");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setRequestProperty("accept", "application/json,text/*");
            http.connect();
            http.getOutputStream().flush();
            show(http.getResponseCode());
            show(http.getResponseMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    //将输入流显示在控制台
    public static void inPrint(InputStream in) {
        int i;
        try (BufferedInputStream is = new BufferedInputStream(in);) {
            while((i = is.read()) != -1) {
                System.out.write(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //控制台输出对象
    public static void show(Object o) {
        System.out.println(o);
    }
}
