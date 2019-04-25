package io.vinson.notebook.redis;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.*;

public class Chapter01 {
    private static final int ONE_WEEK_IN_SECONDS = 7 * 86400;
    private static final int VOTE_SCORE = 432;
    private static final int ARTICLES_PER_PAGE = 25;
    private final static RedissonClient client;
    static {
        RedisConfig config = new RedisConfig();
        config.setAddress("redis://127.0.0.1:6379");
        client = RedissionManager.connect(config);
    }

    public static final void main(String[] args) {
        new Chapter01().run();
    }

    public void run() {

        RBucket<String> keyObject = client.getBucket("key");
        keyObject.set("value");
        System.out.println(keyObject.get());
    }

}