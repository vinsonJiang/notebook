package io.vinson.notebook.bioserver;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/4/2
 */
public class BioServer {
    public static final int PORT = 80;
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "WebRoot";
    // 关闭服务命令
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private int port;

    public BioServer(int port) {
        this.port = port;
    }

    // 维护新的连接
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));
            System.out.println("server start .............");

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("接收到一个连接");
                executorService.submit(new ServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BioServer server = new BioServer(PORT);
        server.start();
    }
}
