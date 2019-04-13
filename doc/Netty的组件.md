
## Netty核心组件

Netty是一个基于NIO实现的异步、事件驱动的网络应用框架，Netty是通过各个组件之间相互协调工作的，Nettty 有如下几个核心组件：

- Channel
- ChannelFuture
- EventLoop
- ChannelHandler
- ChannelPipeline

### Channel
基本的 I/O 操作（bind()、connect()、read()和 write()）依赖于底层网络传输所提
供的原语。在基于 Java 的网络编程中，其基本的构造是 class Socket。Netty 的 Channel 接
口所提供的 API，大大地降低了直接使用 Socket 类的复杂性。此外，Channel 也是拥有许多
预定义的、专门化实现的广泛类层次结构的根，下面是一个简短的部分清单：
- EmbeddedChannel；
- LocalServerChannel；
- NioDatagramChannel；
- NioSctpChannel；
- NioSocketChannel

### ChannelFuture

在Netty中，所有的IO操作都是异步的，所以一个IO操作的结果不会立即返回，这就需要一种用于在之后的莫格时间点处理器结果的方法。
在Netty中，通过调用ChannelFuture的addListener方法注册一个ChannelFutureListener，以便在完成io操作时得到通知。


### EventLoop

EventLoop 定义了 Netty 的核心抽象，用于处理连接的生命周期中所发生的事件。

一个EventLoopGroup里包含了多个EventLoop，一个EventLoop在它的生命周期里之和一个Thread绑定，所有的EventLoop处理的IO时间都将在它专有的Thread上被处理。
一个Channel在它的生命周期内只注册一个EventLoop，一个EventLoop可能会被分配给一个或多个Channel。

### ChannelHandler

ChannelHandler是主要面向开发人员的组件的核心接口，它充当了所有处理入站和出站数据的应用程序逻辑的容器。


### ChannelPipeline

ChannelPipeline接口提供了ChannelHandler链的容器，并定义了用于该链上春波入站和出站数据流的api。