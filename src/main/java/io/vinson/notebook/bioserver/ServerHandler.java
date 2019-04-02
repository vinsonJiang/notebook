package io.vinson.notebook.bioserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/4/2
 */
public class ServerHandler implements Runnable {
    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        httpServerHandler(socket);
    }

    private void httpServerHandler(Socket socket) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            Request request = new Request(inputStream);
            request.parse();
            Response response = new Response(outputStream);
            response.setRequest(request);
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
