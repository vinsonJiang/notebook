package io.vinson.notebook.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @Description: jetty启动一个webapp
 * @author: jiangweixin
 * @date: 2019/3/27
 */
public class JettyServer {
    public static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    public static final int MAX_CONTEXT = 8192;
    public static final int MAX_THREAD_POOL = 128;

    public static void main(String[] args) {
        Server server = new Server(new QueuedThreadPool(MAX_THREAD_POOL));
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase("/webapp");
        context.setDescriptor("/web.xml");
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.setMaxFormContentSize(MAX_CONTEXT);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(PORT);
        server.addConnector(connector);
        server.setHandler(context);
        try {
            server.start();
            System.out.println("项目启动，url:" + "http://127.0.0.1:" + PORT + '/');
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }

    }
}
