

## Jetty启动过程

Jetty 服务器的核心启动类是Server，Server类启动成功，就表示服务器启动完成。
通过在启动的时候调用其他组件的start方法，可以方便的对Jetty服务器进行扩展。

由于Jetty的所有组件的生命周期都是基于观察者设计的，所有，Jetty的组件都实现了LifeCycle接口，Server 的 start 方法调用就会调用所有已经注册到 Server 的组件，Server 启动其它组件的顺序是：首先启动设置到 Server 的 Handler，通常这个 Handler 会有很多子 Handler，这些 Handler 将组成一个 Handler 链。Server 会依次启动这个链上的所有 Handler。

接着会启动注册在 Server 上 JMX 的 Mbean，让 Mbean 也一起工作起来，最后会启动 Connector，打开端口，接受客户端请求。

## 处理请求

Jetty 可以作为一个独立的 Servlet 引擎独立提供 Web 服务，但是它也可以与其他 Web 应用服务器集成，所以它可以提供基于两种协议工作，一个是 HTTP，一个是 AJP 协议。
如果将 Jetty 集成到 Jboss 或者 Apache，那么就可以让 Jetty 基于 AJP 模式工作。

#### 1.基于`Http`协议工作

如果前端没有其它 web 服务器，那么 Jetty 应该是基于 HTTP 协议工作。也就是当 Jetty 接收到一个请求时，必须要按照 HTTP 协议解析请求和封装返回的数据。

通过设置 Jetty 的 Connector 实现类为`org.eclipse.jetty.server.bi.SocketConnector`让 Jetty 以 BIO 的方式工作，Jetty 在启动时将会创建 BIO 的工作环境，它会创建 HttpConnection 类用来解析和封装 HTTP1.1 的协议，ConnectorEndPoint 类是以 BIO 的处理方式处理连接请求，ServerSocket 是建立 socket 连接接受和传送数据，Executor 是处理连接的线程池，它负责处理每一个请求队列中任务。
acceptorThread 是监听连接请求，一有 socket 连接，它将进入下面的处理流程。

Jetty也支持基于NIO的工作方式， Jetty 的默认 connector 就是 NIO 方式，其实现类是`org.eclipse.jetty.server.ServerConnector`

当 socket 被真正执行时，HttpConnection 将被调用，这里定义了如何将请求传递到 servlet 容器里，以及如何将请求最终路由到目的 servlet

Jetty创建连接步骤如下：

- 创建一个队列线程池，用于处理每个建立连接产生的任务，这个线程池可以由用户来指定
- 创建 ServerSocket，用于准备接受客户端的 socket 请求，以及客户端用来包装这个 socket 的一些辅助类。
- 创建一个或多个监听线程，用来监听访问端口是否有连接进来。

```java
public class JettyServer {
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webapp";;
    public static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    public static final int MAX_CONTEXT = 8192;
    public static final int MAX_THREAD_POOL = 128;

    public static void main(String[] args) {
        Server server = new Server(new QueuedThreadPool(MAX_THREAD_POOL));
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase(WEB_ROOT);
        context.setDescriptor("/web.xml");
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.setMaxFormContentSize(MAX_CONTEXT);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(PORT);
        server.addConnector(connector);
        server.setHandler(context);
        try {
            server.start();
            System.out.println("Jetty started on: " + "http://127.0.0.1:" + PORT + '/');
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
```

#### 2.基于`AJP`工作模式

这种工作模式下下 servlet 引擎就不需要解析和封装返回的 HTTP 协议，因为 HTTP 协议的解析工作已经在 Apache 或 Nginx 服务器上完成了，Jboss 只要基于更加简单的 AJP 协议工作就行了，这样能减少处理协议的开销，加快请求的响应速度。

AJP的工作流程和Http几乎一致。与 HTTP 方式唯一不同的地方的就是将 SocketConnector 类替换成了 Ajp13SocketConnector。

