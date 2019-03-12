package io.vinson.notebook.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        if(webSocketFrame instanceof TextWebSocketFrame) {
            String receive = ((TextWebSocketFrame) webSocketFrame).text();
            logger.info("{} received {}" + channelHandlerContext.name() + receive);
            channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("【收到消息{" + receive + "}】"));
        } else {
            String message = "不支持该消息类型：" + webSocketFrame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }
}
