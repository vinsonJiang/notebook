package io.vinson.notebook.mq.kafka.builder;

import io.vinson.notebook.mq.kafka.enums.Acks;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KafkaProducerBuilder extends KafkaGenericBuilder<KafkaProducerBuilder> {
    public KafkaProducerBuilder(String bootstrapServers) {
        super(bootstrapServers);
    }

    public KafkaProducerBuilder withBatchSize(int batchSize) {
        return this.withConfiguration("batch.size", batchSize);
    }

    public KafkaProducerBuilder withAcks(Acks acks) {
        return this.withConfiguration("acks", acks.value());
    }

    public KafkaProducerBuilder withLinger(Duration linger) {
        return this.withConfiguration("linger.ms", linger.toMillis());
    }

    public KafkaProducerBuilder withDeliveryTimeout(Duration timeout) {
        return this.withConfiguration("delivery.timeout.ms", timeout.toMillis());
    }

    public KafkaProducerBuilder withMaxRequestSize(int size) {
        return this.withConfiguration("max.request.size", size);
    }

    public KafkaProducerBuilder withMaxBlock(Duration duration) {
        return this.withConfiguration("max.block.ms", duration.toMillis());
    }

    public KafkaProducerBuilder withBufferMemory(int byteSize) {
        return this.withConfiguration("buffer.memory", byteSize);
    }

    public KafkaProducerBuilder withCompressionType(CompressionType type) {
        return this.withConfiguration("compression.type", type.name);
    }

    public KafkaProducerBuilder withMaxInFlightRequestsPerConnection(int requestNumber) {
        return this.withConfiguration("max.in.flight.requests.per.connection", requestNumber);
    }

    public KafkaProducerBuilder withPartitionerClass(Partitioner partitioner) {
        return this.withConfiguration("partitioner.class", partitioner.getClass().getName());
    }

    public KafkaProducerBuilder withInterceptorClasses(ProducerInterceptor... interceptors) {
        List<String> classNames = new ArrayList();

        for(int i = 0; i < interceptors.length; ++i) {
            ProducerInterceptor interceptor = interceptors[i];
            classNames.add(interceptor.getClass().getName());
        }

        return this.withConfiguration("interceptor.classes", classNames);
    }

    public KafkaProducerBuilder withEnableIdempotence(boolean enabled) {
        return this.withConfiguration("enable.idempotence", enabled);
    }

    public KafkaProducerBuilder withTransactionTimeout(Duration timeout) {
        return this.withConfiguration("transaction.timeout.ms", timeout.toMillis());
    }

    public KafkaProducerBuilder withTransactionalId(String id) {
        return this.withConfiguration("transactional.id", id);
    }

    public <K, V> KafkaProducer<K, V> build(Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        Map<String, Object> allConfigs = this.toConfigurationMap();
        return new KafkaProducer(allConfigs, keySerializer, valueSerializer);
    }

    public KafkaProducer<byte[], byte[]> build() {
        return this.build(new ByteArraySerializer(), new ByteArraySerializer());
    }

    public KafkaProducer<String, String> buildStringProducer() {
        return this.build(new StringSerializer(), new StringSerializer());
    }

    public KafkaProducer<String, String> buildStringProducer(String charset) {
        return (this.withConfiguration("serializer.encoding", charset)).buildStringProducer();
    }
}
