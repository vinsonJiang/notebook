package io.vinson.notebook.java;

import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

/**
 * @author: jiangweixin
 * @date: 2019/4/24
 */
public class InitSequenceBean implements InitializingBean {

    static {
        System.out.println("execute static");
    }

    public InitSequenceBean() {
        System.out.println("execute construct");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("execute post construct");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("execute afterPropertiesSet");
    }

    public void test() {
        System.out.println("execute test");
    }

    public static void main(String[] args) {
        InitSequenceBean bean = new InitSequenceBean();
        bean.test();
    }

}
