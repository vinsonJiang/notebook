package io.vinson.notebook.netty.telnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/3/21
 */
public class TelnetClient {
    private static final Logger logger = LoggerFactory.getLogger(TelnetClient.class);

    public static final boolean SSL = System.getProperty("ssl") != null;
    public static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8081"));

}
