//package io.vinson.notebook.mq.kafka.builder;
//
//import org.apache.kafka.clients.admin.KafkaAdminClient;
//import org.apache.kafka.common.security.auth.SecurityProtocol;
//import org.springframework.util.StringUtils;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public abstract class KafkaGenericBuilder<T extends KafkaGenericBuilder> {
//    private Map<String, Object> configurations;
//
//    public KafkaGenericBuilder(String bootstrapServers) {
//        if (StringUtils.isEmpty(bootstrapServers)) {
//            throw new IllegalArgumentException("kafka bootstrapServers must not empty.");
//        } else {
//            this.configurations = new ConcurrentHashMap();
//            this.configurations.put("bootstrap.servers", bootstrapServers);
//        }
//    }
//
//    public T withClientId(String clientId) {
//        return this.withConfiguration("client.id", clientId);
//    }
//
//    public T withMetaDataMaxAge(Duration dataMaxAge) {
//        long milliSeconds = dataMaxAge.toMillis();
//        return this.withConfiguration("metadata.max.age.ms", milliSeconds);
//    }
//
//    public T withSendBufferBytes(int bufferBytes) {
//        return this.withConfiguration("send.buffer.bytes", bufferBytes);
//    }
//
//    public T withReceiveBufferBytes(int bufferBytes) {
//        return this.withConfiguration("receive.buffer.bytes", bufferBytes);
//    }
//
//    public T withReconnectBackOff(Duration backOff) {
//        return this.withConfiguration("reconnect.backoff.ms", backOff.toMillis());
//    }
//
//    public T withReconnectBackOffMax(Duration backOff) {
//        return this.withConfiguration("reconnect.backoff.max.ms", backOff.toMillis());
//    }
//
//    public T withRetries(int retries) {
//        return this.withConfiguration("retries", retries);
//    }
//
//    public T withRetryBackOff(Duration backOff) {
//        return this.withConfiguration("retry.backoff.ms", backOff.toMillis());
//    }
//
//    public T withSecurityProtocol(SecurityProtocol protocol) {
//        return this.withConfiguration("security.protocol", protocol.name);
//    }
//
//    public T withConnectionsMaxIdle(Duration maxIdle) {
//        return this.withConfiguration("connections.max.idle.ms", maxIdle.toMillis());
//    }
//
//    public T withRequestTimeout(Duration timeout) {
//        return this.withConfiguration("request.timeout.ms", timeout.toMillis());
//    }
//
//    public KafkaConsumerBuilder toConsumerBuilder() {
//        return (KafkaConsumerBuilder)(new KafkaConsumerBuilder(this.bootstrapServers())).withConfigurations(this.toConfigurationMap());
//    }
//
//    public KafkaProducerBuilder toProducerBuilder() {
//        return (KafkaProducerBuilder)(new KafkaProducerBuilder(this.bootstrapServers())).withConfigurations(this.toConfigurationMap());
//    }
//
//    public KafkaAdminClient buildAdminClient() {
//        return (KafkaAdminClient)KafkaAdminClient.create(this.toConfigurationMap());
//    }
//
//    public T withConfiguration(String name, Object value) {
//        this.configurations.put(name, value);
//        return this;
//    }
//
//    public T withConfigurations(Map<String, Object> values) {
//        if (values != null && !values.isEmpty()) {
//            this.configurations.putAll(values);
//        }
//
//        return this;
//    }
//
//    public Map<String, Object> toConfigurationMap() {
//        return Collections.unmodifiableMap(this.configurations);
//    }
//
//    public String bootstrapServers() {
//        return (String)this.configurations.get("bootstrap.servers");
//    }
//}
