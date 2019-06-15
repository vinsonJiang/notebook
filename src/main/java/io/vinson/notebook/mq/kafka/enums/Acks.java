package io.vinson.notebook.mq.kafka.enums;

public enum Acks {
    ALL("all"),
    NEGATIVE_ONE("-1"),
    ZERO("0"),
    ONE("1");

    private String value;

    private Acks(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
