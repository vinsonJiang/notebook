package io.vinson.notebook.redis;

import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Redis 管理器
 * @author: jiangweixin
 * @date: 2019/4/19
 */
public final class RedissionManager {

    private static final Logger logger = LoggerFactory.getLogger(RedissionManager.class);

    private static RedissonClient redisson;

    private RedissionManager() {
    }

    public static RedissonClient connect(RedisConfig redisConfig) {
        if(redisson != null) {
            logger.warn("redis client has connected...");
        }
        Config config = new Config();
        config.useSingleServer().setAddress(redisConfig.getAddress())
                .setConnectionMinimumIdleSize(redisConfig.getConnectionMinimumIdleSize())
                .setConnectionPoolSize(redisConfig.getConnectionPoolSize())
                .setDatabase(redisConfig.getDatabase())
                .setDnsMonitoring(redisConfig.isDnsMonitoring())
                .setDnsMonitoringInterval(redisConfig.getDnsMonitoringInterval())
                .setSubscriptionConnectionMinimumIdleSize(redisConfig.getSubscriptionConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(redisConfig.getSubscriptionConnectionPoolSize())
                .setSubscriptionsPerConnection(redisConfig.getSubscriptionsPerConnection())
                .setClientName(redisConfig.getClientName())
                .setFailedAttempts(redisConfig.getFailedAttempts())
                .setRetryAttempts(redisConfig.getRetryAttempts())
                .setRetryInterval(redisConfig.getRetryInterval())
                .setReconnectionTimeout(redisConfig.getReconnectionTimeout())
                .setTimeout(redisConfig.getTimeout())
                .setConnectTimeout(redisConfig.getConnectTimeout())
                .setIdleConnectionTimeout(redisConfig.getIdleConnectionTimeout())
                .setPingTimeout(redisConfig.getPingTimeout())
                .setPassword(redisConfig.getPassword());
        Codec codec= null;
        try {
            codec = (Codec) Class.forName(redisConfig.getCodec()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        config.setCodec(codec);
        config.setThreads(redisConfig.getThread());
        config.setEventLoopGroup(new NioEventLoopGroup());
        config.setUseLinuxNativeEpoll(false);
        redisson = Redisson.create(config);
        return redisson;
    }

    public static RedissonClient getRedissonClient() {
        return redisson;
    }

}