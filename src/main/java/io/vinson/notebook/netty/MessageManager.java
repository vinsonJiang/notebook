package io.vinson.notebook.netty;

import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/3/14
 */
public class MessageManager {

    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public String login(NioSocketChannel channel) {
        long id = SockectHolderMap.createNewSocket(channel);
        channel.writeAndFlush(new TextWebSocketFrame("$#LOGIN#$" + String.valueOf(id)));
        return String.valueOf(id);
    }

    public String sendBroadcast(NioSocketChannel channel, String message) {
        Set<Map.Entry<Long, NioSocketChannel>> list = SockectHolderMap.entrySet();
        String date = formatDate(new Date(), yyyy_MM_dd_HH_mm_ss);
        list.forEach(item -> {
            item.getValue().writeAndFlush(new TextWebSocketFrame(item.getKey() + "|" + date + "|" + message));
        });
        return message;
    }

    public String sendMessageToId(long id, String message) {
        if(!SockectHolderMap.containsKey(id)) {
            return message;
        }
        String date = formatDate(new Date(), yyyy_MM_dd_HH_mm_ss);
        NioSocketChannel channel = SockectHolderMap.get(id);
        channel.writeAndFlush(new TextWebSocketFrame(id + "|" + date + "|" + message));
        return message;
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat tf = new SimpleDateFormat(format);
        return tf.format(date);
    }
}
