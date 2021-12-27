package com.msspring.fangis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

// 4/5 Sterne - Super Service!
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

    @Scheduled(fixedRate = 100)
    public void update() {
        Map<String, PlayerState> players = new HashMap<>();
        for (GameState state : gameManager.getGameStates()) {
            for (Map.Entry<User, Player> e : state.getPlayerMapping().entrySet()) {
                players.put(e.getKey().getName(), new PlayerState(e.getKey(), e.getValue()));
            }
        }
        Player faenger = this.updateFaenger();
        GameStateMessage msg = new GameStateMessage(players.values().toArray(new PlayerState[0]), faenger);
        messagingTemplate.convertAndSend("/game/broadcast", msg);
    }

    private Player updateFaenger() {
        //TODO This Method should compute the actual faenger from the gamestate
        return gameManager.getGameStates()[0].getFaenger();
    }


}



