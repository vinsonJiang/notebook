package io.vinson.notebook.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.io.*;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/3/12
 */
public final class HtmlUtil {

    public static ByteBuf readTextFile(String filePath) {
        File file = new File(filePath);
        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while(line != null) {
                builder.append(line + "\r\n");
                line = reader.readLine();
            }
            return Unpooled.copiedBuffer(builder.toString(), CharsetUtil.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
