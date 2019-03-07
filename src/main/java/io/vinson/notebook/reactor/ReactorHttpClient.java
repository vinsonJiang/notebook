package io.vinson.notebook.reactor;

import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.CountDownLatch;

public class ReactorHttpClient {

    public static void main(String[] args) {
        new ReactorHttpClient().testHttp();
    }

    public void testHttp() {
            String url = "http://www.biadu.com/";
        CountDownLatch latch = new CountDownLatch(1);
        HttpClient client = HttpClient.create();
        Mono<String> mono = client.get()
                .uri(url)
                .responseSingle((response, out) -> out.asString());
        mono.subscribe(content -> {
            System.out.println("1------------------");
            System.out.println(content);
            latch.countDown();
        });
        System.out.println("2------------------");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
