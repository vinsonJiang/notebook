//package io.vinson.notebook.mq.kafka.builder;
//
//public abstract class KafkaSSLBuilder<T extends KafkaSSLBuilder> extends KafkaGenericBuilder<KafkaSSLBuilder<T>> {
//    public KafkaSSLBuilder(String bootstrapServers) {
//        super(bootstrapServers);
//    }
//
//    public T withSslProtocol(String supportedProtocols) {
//        return this.withConfiguration("ssl.protocol", supportedProtocols);
//    }
//
//    public T withSslProvider(String provider) {
//        return this.withConfiguration("ssl.provider", provider);
//    }
//
//    public T withSslCipherSuites(String cipherSuites) {
//        return this.withConfiguration("ssl.cipher.suites", cipherSuites);
//    }
//
//    public T withSslEnableProtocols(String protocols) {
//        return this.withConfiguration("ssl.enabled.protocols", protocols);
//    }
//
//    public T withSslKeyStoreType(String keyStoreType) {
//        return this.withConfiguration("ssl.keystore.type", keyStoreType);
//    }
//
//    public T withSslKeyStoreLocation(String location) {
//        return this.withConfiguration("ssl.keystore.location", location);
//    }
//
//    public T withSslKeyStorePassword(String password) {
//        return this.withConfiguration("ssl.keystore.password", password);
//    }
//
//    public T withSslKeyPassword(String password) {
//        return this.withConfiguration("ssl.key.password", password);
//    }
//
//    public T withSslTrustStoreType(String storeType) {
//        return this.withConfiguration("ssl.truststore.type", storeType);
//    }
//
//    public T withSslTrustStoreLocation(String location) {
//        return this.withConfiguration("ssl.truststore.location", location);
//    }
//
//    public T withSslTrustStorePassword(String password) {
//        return this.withConfiguration("ssl.truststore.password", password);
//    }
//
//    public T withSslKeyManagerAlgorithm(String algorithm) {
//        return this.withConfiguration("ssl.keymanager.algorithm", algorithm);
//    }
//
//    public T withSslTrustManagerAlgorithm(String algorithm) {
//        return this.withConfiguration("ssl.keymanager.algorithm", algorithm);
//    }
//
//    public T withSslEndpointIdentificationAlgorithm(String algorithm) {
//        return this.withConfiguration("ssl.endpoint.identification.algorithm", algorithm);
//    }
//
//    public T withSslSecureRandomImplementation(String implementation) {
//        return this.withConfiguration("ssl.secure.random.implementation", implementation);
//    }
//
//    public T withConfiguration(String name, Object value) {
//        return (KafkaSSLBuilder)super.withConfiguration(name, value);
//    }
//}
