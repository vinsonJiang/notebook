package io.vinson.notebook.nioserver;

import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by JiangWeixin on 2019/3/27.
 */
public class NIOServer implements Runnable {

    public static Logger logger = LoggerFactory.getLogger(NIOServer.class);

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webapp";;

    Selector selector;
    ServerSocketChannel serverSocket;
    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    public NIOServer(int port) throws Exception {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void run() {
        while(!Thread.interrupted()) {
            try {
                selector.select();

                Set set = selector.selectedKeys();
                Iterator it = set.iterator();
                while(it.hasNext()) {
                    dispatch((SelectionKey)it.next());
                    it.remove();;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatch(SelectionKey key) throws IOException {
        if(key.isAcceptable()) {
            register(key);
        } else if(key.isReadable()) {
            read(key);
        } else if(key.isWritable()) {
            write(key);
        }
    }

    private void write(SelectionKey key) {
//        System.out.println("读数据");
    }

    private void read(SelectionKey key) throws IOException {
        logger.info("读取到数据");
        SocketChannel channel = (SocketChannel) key.channel();

        Request request = handleRequest(channel);
        if(!channel.isOpen()) {
            return;
        }
        Response response = handleResponse(channel, request);
        response.sendStaticResource();
    }

    @Deprecated
    private void readTest(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        logger.info("读取到数据");
//        ByteBuffer readBuffer = null;
        /*当客户端channel关闭后，会不断收到read事件，但没有消息，即read方法返回-1
         * 所以这时服务器端也需要关闭channel，避免无限无效的处理*/
        int count = channel.read(readBuffer);

        if (count > 0) {
            //一定需要调用flip函数，否则读取错误数据
            readBuffer.flip();
            /*使用CharBuffer配合取出正确的数据
            String question = new String(readBuffer.array());
            可能会出错，因为前面readBuffer.clear();并未真正清理数据
            只是重置缓冲区的position, limit, mark，
            而readBuffer.array()会返回整个缓冲区的内容。
            decode方法只取readBuffer的position到limit数据。
            例如，上一次读取到缓冲区的是"where", clear后position为0，limit为 1024，
            再次读取“bye"到缓冲区后，position为3，limit不变，
            flip后position为0，limit为3，前三个字符被覆盖了，但"re"还存在缓冲区中，
            所以 new String(readBuffer.array()) 返回 "byere",
            而decode(readBuffer)返回"bye"。
            */
            CharBuffer charBuffer = CharsetUtil.decoder(Charset.defaultCharset()).decode(readBuffer);
            String question = charBuffer.toString();
            System.out.println(question);
            channel.write(CharsetUtil.encoder(Charset.defaultCharset()).encode(CharBuffer.wrap("this is server's answer")));
        } else {
            channel.close();
        }
    }

    private void register(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();

        SocketChannel channel = server.accept();
        channel.configureBlocking(false);

        channel.register(selector, SelectionKey.OP_READ);
    }


    private Request handleRequest(SocketChannel channel) {
        Request request = new Request(channel);
        try {
            request.handleContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    private Response handleResponse(SocketChannel channel, Request request) {
        Response response = new Response(channel);
        response.setRequest(request);
        return response;
    }

    public static void main(String[] args) {
        try {
            NIOServer server = new NIOServer(90);
            Thread thread = new Thread(server);
            thread.start();
            logger.info("项目启动，url:" + "http://127.0.0.1:" + 90 + '/');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
