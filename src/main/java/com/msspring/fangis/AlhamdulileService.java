package com.msspring.fangis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// 5/5 Sterne - Super Service!
@Component
public class AlhamdulileService {
    private Map<String, User> userMapping;
    private GameManager gameManager;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AlhamdulileService(Map<String, User> userMapping, GameManager gameManager, SimpMessagingTemplate messagingTemplate) {
        this.userMapping = userMapping;
        this.gameManager = gameManager;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 500)
    public void update() {
        System.out.println(userMapping);
        List<PlayerState> players = new ArrayList<>();
        for (GameState state : gameManager.getGameStates()) {
            for (Map.Entry<User, Player> e : state.getPlayerMapping().entrySet()) {
                players.add(new PlayerState(e.getKey(), e.getValue()));
            }
        }
        System.out.println(Arrays.toString(players.toArray(PlayerState[]::new)));
        messagingTemplate.convertAndSend("/topic/greetings", new GameStateMessage(players.get(0))); //.toArray(PlayerState[]::new)));
    }
}



