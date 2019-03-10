package io.vinson.notebook.webflux.service;

import io.vinson.notebook.webflux.bean.Player;
import io.vinson.notebook.webflux.dao.PlayerDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerDataBase playerDataBase;

    public void addPlayer(Player player) {
        playerDataBase.insertPlayer(player);
    }

    public Mono<Player> getPlayer(int id) {
        Player player = playerDataBase.selectPlayerById(id);
        return Mono.just(player);
    }

    public Flux<Player> getPlayerList() {
        List<Player> list = playerDataBase.selectAllPlayers();
        return Flux.fromStream(list.stream());
    }
}
