package io.vinson.notebook.reactor;

import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/3/2
 */
public class GameCalculation {

    public static void main(String[] args) {
        GameCalculation gc = new GameCalculation();
        gc.test();
    }

    private static int total = 0;

    public void test() {
        CountDownLatch latch = new CountDownLatch(1);

        Flux.create(sink -> {
            new Thread(() -> {
                sink.next(GameEvent.builder().num(2).player(Player.builder().name("123").attack(5).build()).build());
                sink.complete();
            }).start();
        })
            .map(GameEvent -> (GameEvent) GameEvent)
            .filter(GameEvent -> GameEvent.getNum() != 0)
            .map(GameEvent -> {
                GameCalculation.total = GameCalculation.total + GameEvent.getPlayer().getAttack() * GameEvent.getNum();
                return GameCalculation.total;
            })
            .subscribe(t -> {
                int sum = t > 100 ? t - 10 : t;
                System.out.println("总共战力和:" + sum);
                latch.countDown();
            });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Data
    @Builder
    static class GameEvent {

        private Player player;

        private int num;
    }

    @Data
    @Builder
    static class Player {

        private String name;

        private int attack;
    }

}
