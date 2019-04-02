package io.vinson.notebook.bioserver;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/4/2
 */
public class Request {

    private InputStream inputStream;

    private String url;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    //从InputStream中读取request信息，并从request中获取uri值
    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        System.out.print(request.toString());
        this.url = parseUri(request.toString());
    }

    /**
     * 获取url
     */
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

}

