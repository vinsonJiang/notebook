package io.vinson.notebook.nioserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by JiangWeixin on 2019/3/27.
 */
public class Request {
    public static Logger logger = LoggerFactory.getLogger(Request.class);

    public static final int BUFFER_SIZE = 1024;

    private String url;

    private SocketChannel socketChannel;

    private String context;

    public Request(SocketChannel channel) {
        this.socketChannel = channel;
    }

    public String getUrl() {
        return url;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    /**
     * http协议请求格式如下：
     *
     * GET / HTTP/1.1
     * Host: localhost:90
     * Connection: keep-alive
     * Cache-Control: max-age=0
     * Upgrade-Insecure-Requests: 1
     * User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36
     * Accept: text/html,application/xhtml+xml,application/xml;
     *
     */
    public void handleContext() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        byteBuffer.clear();
        int length = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((length = socketChannel.read(byteBuffer)) > 0) {
            stringBuilder.append(new String(byteBuffer.array(), 0, length));
        }

        // 当客户端channel关闭后，会不断收到read事件，但没有消息，即read方法返回-1
        // 所以服务器端也需要关闭channel，避免无限处理无效请求
        if(length < 0) {
            socketChannel.close();
            return;
        }
        this.context = stringBuilder.toString();

        logger.info(this.context);

        if (this.context.trim().equals("")) {
            throw new Exception("context is null");
        }
        parseUrl();
    }

    private void parseUrl() {
        int index1, index2;
        index1 = context.indexOf(' ');
        if (index1 != -1) {
            index2 = context.indexOf(' ', index1 + 1);
            if (index2 > index1)
                this.url = context.substring(index1 + 1, index2);
        }
        logger.debug("url is:" + this.url);
//        if(this.url.equals("/")){
//            this.url="/index.html";
//        }
    }
}
