package io.vinson.notebook.mq.kafka.builder;

import java.util.Map;

/**
 * @author: jiangweixin
 * @date: 2019/6/15
 */
public class KafkaClientBuilder {
    public KafkaClientBuilder() {
    }

    public static KafkaClientBuilder.KafkaRegularBuilder withBootstrapServers(String servers) {
        return new KafkaClientBuilder.KafkaRegularBuilder(servers);
    }

    public static KafkaClientBuilder.KafkaRegularSSLBuilder withBootstrapServersForSsl(String servers) {
        return new KafkaClientBuilder.KafkaRegularSSLBuilder(servers);
    }

    public static class KafkaRegularSSLBuilder extends KafkaSSLBuilder<KafkaClientBuilder.KafkaRegularSSLBuilder> {
        private KafkaRegularSSLBuilder(String bootstrapServers) {
            super(bootstrapServers);
        }

        public KafkaClientBuilder.KafkaRegularSSLBuilder withConfiguration(String name, Object value) {
            return (KafkaClientBuilder.KafkaRegularSSLBuilder)super.withConfiguration(name, value);
        }

        public KafkaClientBuilder.KafkaRegularSSLBuilder withConfigurations(Map<String, Object> values) {
            return (KafkaClientBuilder.KafkaRegularSSLBuilder)super.withConfigurations(values);
        }
    }

    public static class KafkaRegularBuilder extends KafkaGenericBuilder<KafkaClientBuilder.KafkaRegularBuilder> {
        private KafkaRegularBuilder(String servers) {
            super(servers);
        }

        public KafkaClientBuilder.KafkaRegularSSLBuilder toSslBuilder() {
            return (new KafkaClientBuilder.KafkaRegularSSLBuilder(this.bootstrapServers())).withConfigurations(this.toConfigurationMap());
        }
    }
}
