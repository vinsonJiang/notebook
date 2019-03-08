package io.vinson.notebook.reactor;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import reactor.core.publisher.Flux;
import reactor.netty.Connection;
import reactor.netty.resources.LoopResources;
import reactor.netty.udp.UdpClient;
import reactor.netty.udp.UdpServer;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReactorUdpServer {
    public static void main(String[] args) {

    }

    public void testUdpServer() {
        LoopResources resources = LoopResources.create("hello");
        CountDownLatch latch = new CountDownLatch(1);

        Connection server = UdpServer.create()
            .port(0)
            .runOn(resources)
            .handle((in, out) ->
                in.receiveObject()
                    .map(d -> {
                        DatagramPacket packet = (DatagramPacket) d;
                        ByteBuf buffer = packet.content();
                        System.out.println("server received : " + buffer.readCharSequence(buffer.readableBytes(), CharsetUtil.UTF_8));
                        ByteBuf buf1 = Unpooled.copiedBuffer("", CharsetUtil.UTF_8);
                        ByteBuf buf2 = Unpooled.copiedBuffer(buf1, buffer);
                        buf1.release();
                        return new DatagramPacket(buf2, packet.sender());
                    })
                    .flatMap(obj -> {
                        latch.countDown();
                        return out.sendObject(obj);
                    })
            )
            .wiretap(true)
            .bind()
            .block(Duration.ofSeconds(30));


        Connection client = UdpClient.create()
                .port(server.address().getPort())
                .runOn(resources)
                .handle((in, out) -> out.sendString(Flux.just("hello server")))
                .wiretap(true)
                .connect()
                .block();
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.disposeNow();
        server.disposeNow();
    }
}
