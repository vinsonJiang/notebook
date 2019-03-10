package io.vinson.notebook.webflux.handler;

import io.vinson.notebook.webflux.bean.Player;
import io.vinson.notebook.webflux.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PlayerHandler {
    @Autowired
    private PlayerService playerService;

    public Mono<ServerResponse> getPlayerById(ServerRequest request) {
        String idStr = request.pathVariable("id");
        Integer id = Integer.valueOf(idStr);
        Mono<Player> playerMono = playerService.getPlayer(id);
        return ServerResponse.ok().body(playerMono, Player.class);
    }

    public Mono<ServerResponse> getPlayerList(ServerRequest request) {
        return null;
    }

}
