package io.vinson.notebook.netty.springboot;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/3/26
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    public static final String WEBSOCKET_PATH = "/ws";

    private final SslContext sslContext;

    public NettyServerInitializer(SslContext sslCtx) {
        sslContext = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        if(sslContext != null) {
            pipeline.addLast(sslContext.newHandler(socketChannel.alloc()));
        }
    }
}
