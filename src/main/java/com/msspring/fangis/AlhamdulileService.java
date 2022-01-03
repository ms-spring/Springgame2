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
        PlayerState faenger = this.updateFaenger();
        GameStateMessage msg = new GameStateMessage(players.values().toArray(new PlayerState[0]), faenger);
        messagingTemplate.convertAndSend("/game/broadcast", msg);
    }

    private PlayerState updateFaenger() {
        User currFaenger = gameManager.getGameStates()[0].getFaenger();
        HashMap<User, Player> playerMapping = gameManager.getGameStates()[0].getPlayerMapping();
        //set initial faenger or reset faenger if faenger left.
        if (currFaenger == null || !playerMapping.containsKey(currFaenger)) {
            User user = playerMapping.keySet().stream().findAny().orElse(null);
            PlayerState initialFaenger = (user==null) ?  null : new PlayerState(user,playerMapping.get(user));
            gameManager.getGameStates()[0].setFaenger(user);
            return initialFaenger;
        }
        //check for faenger change
        User newUser = playerMapping.keySet().stream().filter(user -> user!= currFaenger && playerMapping.get(currFaenger).computeDist(playerMapping.get(user))<=30 ).findAny().orElse(null);
        PlayerState newFaenger = (newUser==null) ?  new PlayerState(currFaenger,playerMapping.get(newUser)) : new PlayerState(newUser,playerMapping.get(newUser));
        gameManager.getGameStates()[0].setFaenger(newUser);
        return newFaenger;

    }


}



