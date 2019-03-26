package io.vinson.notebook.netty.springboot;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/3/26
 */
public class NettyServer {
    public static final boolean SSL = System.getProperty("ssl") != null;
    public static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8081"));

    public void init() {
        SslContext sslCtx = null;
        if (SSL) {
            SelfSignedCertificate ssc = null;
            try {
                ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (SSLException e) {
                e.printStackTrace();
            }
        } else {
            sslCtx = null;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new NettyServerInitializer(sslCtx));

            Channel ch = b.bind(PORT).sync().channel();

            System.out.println("项目启动，url:" + (SSL? "https" : "http") + "://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
