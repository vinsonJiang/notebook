//package io.vinson.notebook.mq.kafka.builder;
//
//import io.vinson.notebook.mq.kafka.enums.AutoOffsetReset;
//import org.apache.kafka.clients.consumer.*;
//import org.apache.kafka.clients.consumer.internals.PartitionAssignor;
//import org.apache.kafka.common.TopicPartition;
//import org.apache.kafka.common.errors.WakeupException;
//import org.apache.kafka.common.requests.IsolationLevel;
//import org.apache.kafka.common.serialization.ByteArrayDeserializer;
//import org.apache.kafka.common.serialization.Deserializer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.Duration;
//import java.util.*;
//import java.util.concurrent.Executor;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.function.BiConsumer;
//import java.util.function.BiFunction;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//public class KafkaConsumerBuilder extends KafkaGenericBuilder<KafkaConsumerBuilder> {
//    public KafkaConsumerBuilder(String bootstrapServers) {
//        super(bootstrapServers);
//    }
//
//    public KafkaConsumerBuilder withGroupId(String groupId) {
//        return (KafkaConsumerBuilder)this.withConfiguration("group.id", groupId);
//    }
//
//    public KafkaConsumerBuilder withMaxPollRecords(int number) {
//        return (KafkaConsumerBuilder)this.withConfiguration("max.poll.records", number);
//    }
//
//    public KafkaConsumerBuilder withMaxPollInterval(Duration interval) {
//        return (KafkaConsumerBuilder)this.withConfiguration("max.poll.interval.ms", interval.toMillis());
//    }
//
//    public KafkaConsumerBuilder withSessionTimeout(Duration timeout) {
//        return (KafkaConsumerBuilder)this.withConfiguration("session.timeout.ms", timeout.toMillis());
//    }
//
//    public KafkaConsumerBuilder withHeartbeatInterval(Duration interval) {
//        return (KafkaConsumerBuilder)this.withConfiguration("heartbeat.interval.ms", interval.toMillis());
//    }
//
//    public KafkaConsumerBuilder withEnableAutoCommit(boolean enabled) {
//        return (KafkaConsumerBuilder)this.withConfiguration("enable.auto.commit", enabled);
//    }
//
//    public KafkaConsumerBuilder withAutoCommitInterval(Duration interval) {
//        return (KafkaConsumerBuilder)this.withConfiguration("auto.commit.interval.ms", interval.toMillis());
//    }
//
//    public KafkaConsumerBuilder withPartitionAssignmentStrategy(PartitionAssignor strategy) {
//        return (KafkaConsumerBuilder)this.withConfiguration("partition.assignment.strategy", strategy.getClass().getName());
//    }
//
//    public KafkaConsumerBuilder withAutoOffsetReset(AutoOffsetReset reset) {
//        return (KafkaConsumerBuilder)this.withConfiguration("auto.offset.reset", reset.name().toLowerCase());
//    }
//
//    public KafkaConsumerBuilder withFetchMinBytes(int minBytes) {
//        return (KafkaConsumerBuilder)this.withConfiguration("fetch.min.bytes", minBytes);
//    }
//
//    public KafkaConsumerBuilder withFetchMaxBytes(int maxBytes) {
//        return (KafkaConsumerBuilder)this.withConfiguration("fetch.max.bytes", maxBytes);
//    }
//
//    public KafkaConsumerBuilder withFetchMaxWait(Duration maxWait) {
//        return (KafkaConsumerBuilder)this.withConfiguration("fetch.max.wait.ms", maxWait.toMillis());
//    }
//
//    public KafkaConsumerBuilder withMaxPartitionFetchBytes(int maxBytes) {
//        return (KafkaConsumerBuilder)this.withConfiguration("max.partition.fetch.bytes", maxBytes);
//    }
//
//    public KafkaConsumerBuilder withCheckCrcs(boolean enabled) {
//        return (KafkaConsumerBuilder)this.withConfiguration("check.crcs", enabled);
//    }
//
//    public KafkaConsumerBuilder withDefaultApiTimeout(Duration timeout) {
//        return (KafkaConsumerBuilder)this.withConfiguration("default.api.timeout.ms", timeout.toMillis());
//    }
//
//    public KafkaConsumerBuilder withInterceptorClasses(ConsumerInterceptor... interceptors) {
//        List<String> classNames = new ArrayList();
//        ConsumerInterceptor[] var3 = interceptors;
//        int var4 = interceptors.length;
//
//        for(int var5 = 0; var5 < var4; ++var5) {
//            ConsumerInterceptor interceptor = var3[var5];
//            classNames.add(interceptor.getClass().getName());
//        }
//
//        return (KafkaConsumerBuilder)this.withConfiguration("interceptor.classes", classNames);
//    }
//
//    public KafkaConsumerBuilder withExcludeInternalTopics(boolean enabled) {
//        return (KafkaConsumerBuilder)this.withConfiguration("exclude.internal.topics", enabled);
//    }
//
//    public KafkaConsumerBuilder withIsolationLevel(IsolationLevel level) {
//        return (KafkaConsumerBuilder)this.withConfiguration("isolation.level", level.name().toLowerCase());
//    }
//
//    public <K, V> KafkaConsumer<K, V> build(Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
//        Map<String, Object> allConfigs = this.toConfigurationMap();
//        return new KafkaConsumer(allConfigs, keyDeserializer, valueDeserializer);
//    }
//
//    public KafkaConsumer<byte[], byte[]> build() {
//        return this.build(new ByteArrayDeserializer(), new ByteArrayDeserializer());
//    }
//
//    public KafkaConsumer<String, String> buildStringConsumer() {
//        return this.build(new StringDeserializer(), new StringDeserializer());
//    }
//
//    public KafkaConsumer<String, String> buildStringConsumer(String charset) {
//        return ((KafkaConsumerBuilder)this.withConfiguration("deserializer.encoding", charset)).buildStringConsumer();
//    }
//
//    public <K, V> KafkaConsumerBuilder.SimpleClientBuilder<K, V> buildToSimpleClient(Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
//        return new KafkaConsumerBuilder.SimpleClientBuilder(this.build(keyDeserializer, valueDeserializer));
//    }
//
//    public KafkaConsumerBuilder.SimpleClientBuilder<byte[], byte[]> buildToSimpleClient() {
//        return new KafkaConsumerBuilder.SimpleClientBuilder(this.build());
//    }
//
//    public KafkaConsumerBuilder.SimpleClientBuilder<String, String> buildToStringSimpleClient() {
//        return new KafkaConsumerBuilder.SimpleClientBuilder(this.buildStringConsumer());
//    }
//
//    public KafkaConsumerBuilder.SimpleClientBuilder<String, String> buildToStringSimpleClient(String charset) {
//        return new KafkaConsumerBuilder.SimpleClientBuilder(this.buildStringConsumer(charset));
//    }
//
//    public static class SimpleClientBuilder<K, V> {
//        private KafkaConsumerBuilder.SimpleClient<K, V> client;
//        private Set<String> subscribes;
//
//        public SimpleClientBuilder(KafkaConsumer<K, V> consumer) {
//            this.client = new KafkaConsumerBuilder.SimpleClient(consumer);
//            this.subscribes = new HashSet();
//        }
//
//        public KafkaConsumerBuilder.SimpleClientBuilder<K, V> subscribe(String topic) {
//            this.subscribes.add(topic);
//            return this;
//        }
//
//        public KafkaConsumerBuilder.SimpleClientBuilder<K, V> subscribe(String... topics) {
//            this.subscribes.addAll(Arrays.asList(topics));
//            return this;
//        }
//
//        public KafkaConsumerBuilder.SimpleClientBuilder<K, V> fromBeginning() {
//            this.client.beginning = true;
//            return this;
//        }
//
//        public KafkaConsumerBuilder.SimpleClientBuilder<K, V> autoClose() {
//            this.client.autoClose = true;
//            return this;
//        }
//
//        public KafkaConsumerBuilder.SimpleClientBuilder<K, V> withEachPoolTimeout(long timeoutMs) {
//            this.client.POLL_TIME_OUT = timeoutMs;
//            return this;
//        }
//
//        public KafkaConsumerBuilder.SimpleClientBuilder<K, V> withOnPartitionAssigned(Consumer<Collection<TopicPartition>> onAssigned) {
//            this.client.onAssigned = onAssigned;
//            return this;
//        }
//
//        public KafkaConsumerBuilder.SimpleClient<K, V> buildWithRecordsCallback(BiConsumer<K, V> recordCallback) {
//            this.client.callBack = (consumer, records) -> {
//                if (!records.isEmpty()) {
//                    Iterator var3 = records.iterator();
//
//                    while(var3.hasNext()) {
//                        ConsumerRecord<K, V> record = (ConsumerRecord)var3.next();
//                        recordCallback.accept(record.key(), record.value());
//                    }
//                }
//
//                return false;
//            };
//            this.doSubscribe();
//            return this.client;
//        }
//
//        public KafkaConsumerBuilder.SimpleClient<K, V> buildWithRecordsCallback(Consumer<ConsumerRecords<K, V>> recordsCallback) {
//            this.client.callBack = (consumer, records) -> {
//                recordsCallback.accept(records);
//                return false;
//            };
//            this.doSubscribe();
//            return this.client;
//        }
//
//        public KafkaConsumerBuilder.SimpleClient<K, V> buildWithRecordsLoopCallback(Function<ConsumerRecords<K, V>, Boolean> recordsCallback) {
//            this.client.callBack = (consumer, records) -> {
//                return (Boolean)recordsCallback.apply(records);
//            };
//            this.doSubscribe();
//            return this.client;
//        }
//
//        public KafkaConsumerBuilder.SimpleClient<K, V> buildWithConsumerRecordsLoopCallback(BiFunction<KafkaConsumer<K, V>, ConsumerRecords<K, V>, Boolean> recordsCallback) {
//            this.client.callBack = recordsCallback;
//            this.doSubscribe();
//            return this.client;
//        }
//
//        private void doSubscribe() {
//            if (this.subscribes != null && !this.subscribes.isEmpty()) {
//                this.client.consumer().subscribe(this.subscribes, this.client.rebalanceListener());
//            } else {
//                throw new MissingFormatArgumentException("Subscribe topics not found.");
//            }
//        }
//    }
//
//    public static class SimpleClient<K, V> implements Runnable, AutoCloseable {
//        private static final Logger log = LoggerFactory.getLogger(KafkaConsumerBuilder.SimpleClient.class);
//        private KafkaConsumer<K, V> consumer;
//        private boolean beginning;
//        private boolean autoClose;
//        private boolean running;
//        private volatile boolean terminated;
//        private long POLL_TIME_OUT;
//        private BiFunction<KafkaConsumer<K, V>, ConsumerRecords<K, V>, Boolean> callBack;
//        private ConsumerRebalanceListener rebalanceListener;
//        private AtomicInteger state;
//        private Consumer<Collection<TopicPartition>> onAssigned;
//
//        private SimpleClient(KafkaConsumer<K, V> consumer) {
//            this.beginning = false;
//            this.autoClose = false;
//            this.running = false;
//            this.terminated = false;
//            this.POLL_TIME_OUT = 600L;
//            this.state = new AtomicInteger(0);
//            this.consumer = consumer;
//            this.rebalanceListener = new ConsumerRebalanceListener() {
//                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
//                    SimpleClient.this.state.set(1);
//                }
//
//                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
//                    SimpleClient.this.state.set(2);
//                    if (SimpleClient.this.onAssigned != null) {
//                        try {
//                            SimpleClient.this.onAssigned.accept(partitions);
//                        } catch (Exception var3) {
//                        }
//                    }
//
//                }
//            };
//        }
//
//        public KafkaConsumer<K, V> consumer() {
//            return this.consumer;
//        }
//
//        private ConsumerRebalanceListener rebalanceListener() {
//            return this.rebalanceListener;
//        }
//
//        public void sync() {
//            this.running = true;
//            this.run();
//        }
//
//        public void async() {
//            this.async("simple-kafka-consumer-client");
//        }
//
//        public void async(String name) {
//            this.running = true;
//            (new Thread(this, name)).start();
//        }
//
//        public void async(Executor executor) {
//            this.running = true;
//            executor.execute(this);
//        }
//
//        public void run() {
//            this.terminated = false;
//
//            try {
//                Duration pollTimeout = Duration.ofMillis(this.POLL_TIME_OUT);
//                if (this.beginning) {
//                    int maxLoop = true;
//                    int currentLoop = 0;
//                    Set partitions = null;
//
//                    do {
//                        this.consumer.poll(this.state.get() < 2 ? pollTimeout : Duration.ZERO);
//                        if (this.state.get() >= 2) {
//                            break;
//                        }
//
//                        ++currentLoop;
//                    } while(currentLoop < 3);
//
//                    partitions = this.consumer.assignment();
//                    if (partitions != null && !partitions.isEmpty()) {
//                        this.consumer.seekToBeginning(partitions);
//                        this.beginning = false;
//                    } else {
//                        log.warn("Unable seek to beginning of consumer subscription {}. no assignments fetched.", this.consumer.subscription());
//                    }
//                }
//
//                while(!this.terminated) {
//                    try {
//                        ConsumerRecords<K, V> records = this.consumer.poll(pollTimeout);
//                        if (this.callBack != null) {
//                            Boolean breakNow = (Boolean)this.callBack.apply(this.consumer, records);
//                            if (breakNow) {
//                                break;
//                            }
//                        }
//                    } catch (WakeupException var27) {
//                    } catch (IllegalStateException var28) {
//                        if (this.consumer.subscription().isEmpty()) {
//                            log.info("No kafka topic consumed. subscription is empty.");
//                            this.terminated = true;
//                        } else {
//                            log.error("Kafka consume state error: " + var28.getMessage(), var28);
//                        }
//                    } catch (Throwable var29) {
//                        log.error("kafka consume data error: " + var29.getMessage(), var29);
//                    }
//                }
//            } finally {
//                try {
//                    this.consumer.commitSync();
//                } finally {
//                    log.info("Kafka consumer {} is going to shutdown.", this.consumer.subscription());
//                    if (this.autoClose) {
//                        this.termination();
//                    }
//
//                }
//            }
//
//        }
//
//        public void stop() {
//            this.terminated = true;
//        }
//
//        public boolean isRunning() {
//            return this.running;
//        }
//
//        public void close() {
//            this.stop();
//        }
//
//        private void termination() {
//            this.running = false;
//            if (this.consumer != null) {
//                this.terminated = true;
//
//                try {
//                    this.consumer.wakeup();
//                } finally {
//                    this.consumer.close();
//                }
//            }
//
//        }
//    }
//}
