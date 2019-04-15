## Netty的启动

Netty的启动类分为两大类，一种是客户端`Bootstrap`，另一种是服务端`ServerBootstrap`;
这两类主要有2个区别
- 1.`ServerBootstrap`绑定一个端口，进行连接监听，`Bootstrap`则是建立与远程服务端的连接。

- 2.`Bootstrap`只需要一个EventLoopGroup组件，而`ServerBootstrap`则需要2个
（也可以是一个，这个时候两个场景共用一个EventLoopGroup）；

### 代码示例：
> 启动一个Http服务器

#### 服务端
``` java
public class HttpServer {
    public static final int PORT = Integer.parseInt(System.getProperty("port", "8081"));

    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 设置1个线程
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 默认为1个线程
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpServerExpectContinueHandler());
                            // TODO: 处理Http请求

                        }
                    });

            Channel ch = b.bind(PORT).sync().channel();

            System.err.println("项目启动在： http://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
```
