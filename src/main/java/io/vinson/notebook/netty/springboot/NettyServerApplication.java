package io.vinson.notebook.netty.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/3/26
 */

@SpringBootApplication
//@ComponentScan(value = "io.vinson.notebook.netty.springboot")
public class NettyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class);
    }
}
