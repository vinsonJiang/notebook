package io.vinson.notebook.webflux.dao;

import io.vinson.notebook.webflux.bean.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerDataBase {

    private static final Map<Integer, Player> playerMap = new ConcurrentHashMap<>();

    static {
        playerMap.put(1, Player.builder().id(1).name("name1").num(100).build());
        playerMap.put(2, Player.builder().id(2).name("name2").num(200).build());
    }

    public void insertPlayer(Player player) {
        playerMap.put(player.getId(), player);
    }

    public Player selectPlayerById(int id) {
        return playerMap.get(id);
    }

    public List<Player> selectAllPlayers() {
        return new ArrayList<>(playerMap.values());
    }
}
