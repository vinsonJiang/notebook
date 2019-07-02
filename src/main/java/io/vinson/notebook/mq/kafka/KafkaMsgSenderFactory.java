//package io.vinson.notebook.mq.kafka;
//
//import io.vinson.notebook.mq.kafka.builder.KafkaClientBuilder;
//import io.vinson.notebook.mq.kafka.builder.KafkaProducerBuilder;
//import org.apache.kafka.clients.producer.KafkaProducer;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author: jiangweixin
// * @date: 2019/6/15
// */
//public class KafkaMsgSenderFactory {
//    public KafkaMsgSenderFactory() {
//    }
//
//    public static SimpleKafkaJsonProducer<String> createProducer(Map<String, String> configs) {
//        String servers = (String)configs.get("kafka.bootstrap.servers");
//        boolean kafkaPrefix = true;
//        if (servers == null) {
//            servers = (String)configs.get("bootstrap.servers");
//            kafkaPrefix = false;
//        }
//
//        if (servers == null) {
//            throw new IllegalArgumentException("Kafka 'bootstrap.servers' required");
//        } else {
//            Map<String, Object> conf = new HashMap();
//            boolean finalKafkaPrefix = kafkaPrefix;
//            configs.forEach((key, value) -> {
//                String name = key;
//                if (key.startsWith("kafka.") && finalKafkaPrefix) {
//                    name = key.substring(6);
//                }
//
//                conf.put(name, value);
//            });
//            KafkaProducer<String, String> kafkaProducer = ((KafkaProducerBuilder)((KafkaClientBuilder.KafkaRegularBuilder) KafkaClientBuilder.withBootstrapServers(servers).withClientId("kafka-log-sender")).toProducerBuilder().withMaxBlock(Duration.ofSeconds(6L)).withEnableIdempotence(true).withConfigurations(conf)).buildStringProducer();
//            return new SimpleKafkaJsonProducer(kafkaProducer);
//        }
//    }
//}
