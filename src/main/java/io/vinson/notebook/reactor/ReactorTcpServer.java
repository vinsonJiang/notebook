package io.vinson.notebook.reactor;

import reactor.core.publisher.Flux;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpClient;
import reactor.netty.tcp.TcpServer;

import java.util.concurrent.CountDownLatch;

public class ReactorTcpServer {

    public static void main(String[] args) {
        new ReactorTcpServer().testTcpServer();
    }

    public void testTcpServer() {
        CountDownLatch latch = new CountDownLatch(1);
        DisposableServer server = TcpServer.create()
                .port(0)
                .handle(((nettyInbound, nettyOutbound) -> {
                    nettyInbound.receive()
                            .asString()
                            .subscribe(t -> {
                                System.out.println("receive: " + t);
                                latch.countDown();
                            });
                    return Flux.never();
                }))
                .wiretap(true)
                .bindNow();
        System.out.println("sever bind port is: " + server.port());

        Connection client = TcpClient.create()
                .port(server.port())
                .handle((nettyInbound, nettyOutbound) ->  nettyOutbound.sendString(Flux.just("hello server")))
                .wiretap(true)
                .connectNow();

        client.disposeNow();
        server.disposeNow();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
