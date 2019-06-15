package io.vinson.notebook.mq.kafka.enums;

public enum AutoOffsetReset {
    LATEST,
    EARLIEST,
    NONE;

    private AutoOffsetReset() {
    }
}
