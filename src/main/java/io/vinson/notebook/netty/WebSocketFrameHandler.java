package io.vinson.notebook.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 处理websocket消息
 * @author: jiangweixin
 * @date: 2019/3/12
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    public static MessageManager messageManager = new MessageManager();

    private int count = 0;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        if(webSocketFrame instanceof TextWebSocketFrame) {
            System.out.println(count++);
            String receive = ((TextWebSocketFrame) webSocketFrame).text();
            NioSocketChannel channel = (NioSocketChannel)channelHandlerContext.channel();
            logger.info("{} received {}", channelHandlerContext.name(), receive);
            if(receive.startsWith("$#LOGIN#$")) {
                messageManager.login(channel);
            } else if(receive.startsWith("$#TO_ID#$")) {
                receive = receive.substring(9, receive.length());
                String[] strs = receive.split("|");
                if(strs == null || strs.length < 2) {
                    throw new UnsupportedOperationException("[" + receive + "] 消息格式错误");
                }
                long id = Long.parseLong(strs[0]);
                messageManager.sendMessageToId(id, receive);
            } else {
                messageManager.sendBroadcast(channel, receive);
            }
        } else {
            String message = "不支持该消息类型：" + webSocketFrame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }
}
