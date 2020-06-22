package com.socket.server.demo;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.socket.thread.demo.RequestProcessorRunable;

/**
 * HTTP完整服务器
 * @author Cherry
 * 2020年5月7日
 */
public class JavaHttpServer {
    private final static Logger logger = Logger.getLogger(JavaHttpServer.class.getCanonicalName());
    private static final int NUM_THREAD = 50;
    private static final String INDEX_FILE = "index.html";
    private File rootDir;
    private int port;
    
    
    public JavaHttpServer(File rootDir, int port) {
        super();
        if(rootDir.isFile()) {
            throw new IllegalArgumentException("please put a directory , not a file!");
        }
        this.rootDir = rootDir;
        this.port = port;
    }

    public void start() {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREAD);
        try(ServerSocket server = new ServerSocket(port);){
            logger.info("Accepting connections on port " + server.getLocalPort());
            logger.info("Document Root :" + rootDir);
            while(true) {
                Socket request = server.accept();
                Runnable task = new RequestProcessorRunable(request, rootDir, INDEX_FILE);
                pool.submit(task);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING,"Error accepting connection",e);
        }
    }

    public static void main(String[] args) {
        new JavaHttpServer(new File("E:/ServiceDemo"), 8080).start();
    }

}
