package io.vinson.notebook.reactor;

import lombok.Data;
import reactor.core.publisher.Flux;

public class ReactorQuery {

    @Data
    static class SimpleBean {
        private int id;

        private String name;

        private float num;
    }

    public Flux<String> queryBeans() {

        return Flux.from(null);
    }
}
