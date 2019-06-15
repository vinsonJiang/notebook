package io.vinson.notebook.mq.kafka.bean;

/**
 * @author: jiangweixin
 * @date: 2019/6/15
 */
public class SimpleBean<K> {
    private K id;

    private String topic;

    private String content;

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
