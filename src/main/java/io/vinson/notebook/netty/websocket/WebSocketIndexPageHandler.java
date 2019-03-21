package io.vinson.notebook.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SystemPropertyUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/3/12
 */
public class WebSocketIndexPageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private final String webSocketPath;

    public WebSocketIndexPageHandler(String webSocketPath) {
        this.webSocketPath = webSocketPath;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {

        if(!request.decoderResult().isSuccess()) {
            sendHttpResponse(channelHandlerContext, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        if(request.method() != HttpMethod.GET) {
            sendHttpResponse(channelHandlerContext, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }

        String filePath = sanitizeUri(request.uri());
        if(filePath == null) {
            sendHttpResponse(channelHandlerContext, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
            return;
        }
        ByteBuf content = HtmlUtil.readTextFile(filePath);
        if(content == null) {
            sendHttpResponse(channelHandlerContext, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
            return;
        }
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

        if(filePath.endsWith(".css")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/css; charset=UTF-8");
        } else if(filePath.endsWith(".js")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/javascript");
        } else {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        }

        HttpUtil.setContentLength(response, content.readableBytes());
        sendHttpResponse(channelHandlerContext, request, response);
    }

    private void sendHttpResponse(ChannelHandlerContext channelHandlerContext, FullHttpRequest request, FullHttpResponse response) {
        if(!response.status().equals(HttpResponseStatus.OK)) {
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(response, response.content().readableBytes());
        }

        ChannelFuture future = channelHandlerContext.channel().writeAndFlush(response);
        if(!HttpUtil.isKeepAlive(request) || !response.status().equals(HttpResponseStatus.OK)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private String getWebSocketUrl(ChannelPipeline pipeline, HttpRequest request, String path) {
        String protocol = "ws";
        if(pipeline.get(SslHandler.class) != null) {
            protocol = "wss";
        }
        return protocol + "://" + request.headers().get(HttpHeaderNames.HOST) + path;
    }

    /**
     * TODO: 不可取，待优化
     * @param uri
     * @return
     */
    private static String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
            if("/".equals(uri)) {
                uri = "/index.html";
            }
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }

        if (uri.isEmpty() || uri.charAt(0) != '/') {
            return null;
        }
        uri = uri.replace('/', File.separatorChar);

        if (uri.contains(File.separator + '.') ||
                uri.contains('.' + File.separator) ||
                uri.charAt(0) == '.' || uri.charAt(uri.length() - 1) == '.' ||
                INSECURE_URI.matcher(uri).matches()) {
            return null;
        }
//        return SystemPropertyUtil.get("user.dir") + File.separator + uri;
        return SystemPropertyUtil.get("user.dir") + "\\src\\main\\resources\\netty" + uri;
    }

}
