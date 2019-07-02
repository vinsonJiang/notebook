package io.vinson.notebook.mq.kafka;

import io.vinson.notebook.common.JSONUtil;
import io.vinson.notebook.mq.kafka.bean.SimpleBean;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: jiangweixin
 * @date: 2019/6/15
 */
public class SimpleKafkaJsonProducer<K> implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(SimpleKafkaJsonProducer.class);
    private KafkaProducer<K, String> producer;
    private Map<Class, Field[]> fieldsMap;

    public SimpleKafkaJsonProducer(KafkaProducer<K, String> producer) {
        this.producer = producer;
        this.fieldsMap = new ConcurrentHashMap();
    }

    public void send(SimpleBean<K> content) {
        K key = content.getId();
        String topic = content.getTopic();
        String json = JSONUtil.toJson(content);
        if (StringUtils.isEmpty(topic)) {
            throw new IllegalArgumentException("EntityMessage topic required for type: " + content.getClass());
        } else {
            this.doSend(key, topic, json);
        }
    }

    public void close() {
        if (this.producer != null) {
            this.producer.close();
        }

    }

    private void doSend(K key, String topic, String json) {
        this.producer.send(new ProducerRecord(topic, key, json), (metadata, exception) -> {
            if (exception != null) {
                if (metadata == null) {
                    log.error("Kafka message send error with key: \"" + key + "\"", exception);
                } else {
                    log.error("Kafka message send error at: " + metadata.topic() + "(" + metadata.partition() + ") with key '" + key + "'", exception);
                }
            }

        });
    }



    private Map<Class, Object> createDefaultValueMap() {
        Map<Class, Object> map = new HashMap();
        map.put(String.class, "");
        return Collections.unmodifiableMap(map);
    }

}
