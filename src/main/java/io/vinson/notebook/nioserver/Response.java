package io.vinson.notebook.nioserver;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;

/**
 * Created by JiangWeixin on 2019/3/27.
 */
public class Response {

    public static final int BUFFER_SIZE = 1024;
    /** http协议响应格式 */
    private static final String RESPONSE_CONTENT = "HTTP/1.1 {0}\r\n" + "Content-Type: text/html\r\n"
            + "Content-Length: {1}\r\n" + "\r\n" + "{2}";


    private Request request;

    private SocketChannel socketChannel;

    public Response(SocketChannel channel) {
        this.socketChannel = channel;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void sendStaticResource() throws IOException {
        FileInputStream fis = null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            String uri = request.getUrl();
            if(uri.equals("/")){
                uri = "/index.html";
            }
            File file = new File(NIOServer.WEB_ROOT, uri);
            System.out.println(file.getAbsolutePath());
            if (file.exists()) {
                String responseBody = MessageFormat.format(RESPONSE_CONTENT, "200 OK", file.length(), "");
                byteBuffer.put(responseBody.getBytes());
                byteBuffer.flip();

                socketChannel.write(byteBuffer);
                byteBuffer.clear();

                fis = new FileInputStream(file);
                FileChannel fileChannel = fis.getChannel();
                fileChannel.transferTo(0, fileChannel.size(), socketChannel);
            } else {
                // file not found
                String responseBody = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
                        + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
                responseBody = MessageFormat.format(RESPONSE_CONTENT, "404 File Not Found", 23, "<h1>File Not Found</h1>");

                byteBuffer.put(responseBody.getBytes());
                byteBuffer.flip();

                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

    }
}
