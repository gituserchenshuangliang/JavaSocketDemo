package com.socket.thread.demo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * ServerSocket记录接受和发送的数据
 * @author Cherry
 * 2020年5月7日
 */
public class GeneralSocketRunnable implements Runnable {
    private final Logger logger = Logger.getLogger(GeneralSocketRunnable.class.getName());
    private Socket socket;
    private String sendMsg;
    public GeneralSocketRunnable(Socket socket,String sendMsg) {
        this.socket = socket;
        this.sendMsg = sendMsg;
    }
    
    @Override
    public void run() {
        try {
            InputStream inp = new BufferedInputStream(socket.getInputStream());
            Reader in = new InputStreamReader(inp, "GBK");
            StringBuilder sb = new StringBuilder(80);
            int c ;
            while((c = in.read()) != -1) {
                sb.append((char)c);
            }
            logger.info("Received message :" + sb.toString());
            
            Writer out = new OutputStreamWriter(socket.getOutputStream(),"GBK");
            out.write(sendMsg);
            out.flush();
            logger.info("Send message :" + sendMsg);
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

}
