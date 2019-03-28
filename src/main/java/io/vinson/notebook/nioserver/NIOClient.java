package io.vinson.notebook.nioserver;

import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by JiangWeixin on 2019/3/27.
 */
public class NIOClient {

    public static void main(String[] args) {
        new NIOClient().start("127.0.0.1", 90);
    }

    public void start(String host, int port) {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(true);

            channel.connect(new InetSocketAddress(host, port));
            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_CONNECT);

            while(true) {
                selector.select();
                Iterator it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    it.remove();
                    if (key.isConnectable()) {
                        if (channel.isConnectionPending()) {
                            if (channel.finishConnect()) {
                                key.interestOps(SelectionKey.OP_READ);

                                channel.write(CharsetUtil.encoder(Charset.defaultCharset()).encode(CharBuffer.wrap("this is client's question")));
                                try {
                                    TimeUnit.SECONDS.sleep(new Random().nextInt(3));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                key.channel();
                            }
                        }
                    } else if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel)key.channel();
                        ByteBuffer buff = ByteBuffer.allocate(1024);
                        int count = sc.read(buff);

                        if (count > 0) {
                            buff.flip();
                            CharBuffer charBuffer = CharsetUtil.decoder(Charset.defaultCharset()).decode(buff);
                            String question = charBuffer.toString();
                            System.out.println(question);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
