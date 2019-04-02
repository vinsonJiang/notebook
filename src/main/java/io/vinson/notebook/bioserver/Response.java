package io.vinson.notebook.bioserver;

import java.io.*;
import java.text.MessageFormat;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/4/2
 */
public class Response {
    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    private static final String RESPONSE_CONTENT = "HTTP/1.1 {0}\r\n" + "Content-Type: text/html\r\n"
            + "Content-Length: {1}\r\n" + "\r\n" + "{2}";

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            //将web文件写入到OutputStream字节流中
            File file = new File(BioServer.WEB_ROOT, request.getUrl());
            if (file.exists()) {
                fis = new FileInputStream(file);

//                int length = 0;
//                while ((length = fis.read(bytes)) != -1) {
//                    output.write(bytes, 0, length);
//                }

                String line; // 用来保存每行读取的内容
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                line = reader.readLine(); // 读取第一行
                while (line != null) { // 如果 line 为空说明读完了
                    sb.append(line); // 将读到的内容添加到 buffer 中
                    sb.append("\n"); // 添加换行符
                    line = reader.readLine(); // 读取下一行
                }
                reader.close();
                String text = MessageFormat.format(RESPONSE_CONTENT, "200 OK", sb.toString().length(), sb.toString());
                System.out.println(text);

            } else {
                // file not found
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
                        + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
                errorMessage = MessageFormat.format(RESPONSE_CONTENT, "404 File Not Found", 23, "<h1>File Not Found</h1>");
                output.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            // thrown if cannot instantiate a File object
            System.out.println(e.toString());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
