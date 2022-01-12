package com.msspring.fangis;

import com.msspring.fangis.gameLogic.*;
import com.msspring.fangis.messages.GameStateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

// 4/5 Sterne - Super Service!
@Component
public class UpdateService {
    private final Map<String, User> userMapping;
    private final GameManager gameManager;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public UpdateService(Map<String, User> userMapping, GameManager gameManager, SimpMessagingTemplate messagingTemplate) {
        this.userMapping = userMapping;
        this.gameManager = gameManager;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 100)
    public void update() {
        //update faengers
        updateFaenger();
        Map<String, PlayerState> players = new HashMap<>();
        for (GameState state : gameManager.getGameStates()) {
            for (Map.Entry<User, Player> e : state.getPlayerMapping().entrySet()) {
                players.put(e.getKey().getName(), new PlayerState(e.getKey(), e.getValue()));
            }
        }
        GameStateMessage msg = new GameStateMessage(players.values().toArray(new PlayerState[0]));
        messagingTemplate.convertAndSend("/game/broadcast", msg);
    }

    private void updateFaenger() {
        //repeat for all lobbies
        for(int i = 0; i<3 ; i++) {
            List<User> currFaengers = getCurrFaenger(i);
            List<User> newFaengers = new ArrayList<>();
            HashMap<User, Player> playerMapping = gameManager.getGameStates()[0].getPlayerMapping();
            //check if current Faengers are still in the lobby
            currFaengers.stream().filter(faenger -> playerMapping.containsKey(faenger));
            //set initial faenger if none has been set
            //TODO allow for several faengers to be set
            if (currFaengers.isEmpty()) {
                User user = playerMapping.keySet().stream().findAny().orElse(null);
                if(user!=null) {
                    currFaengers.add(user);
                }
                continue;
            }
            for(User faenger : currFaengers) {
                //check if the user is fungible (time constraint)
                if (System.currentTimeMillis() - playerMapping.get(faenger).getWhenFunged() < 2000) {
                    continue;
                }
                //Check for faenger change
                User newUser = null;
                //check for faenger change
                newUser = playerMapping.keySet().stream().filter(user -> user != faenger && !playerMapping.get(user).isIsfaenger() && playerMapping.get(faenger).computeDist(playerMapping.get(user)) <= 30).findAny().orElse(null);
                if (newUser == null) {
                    newFaengers.add(faenger);
                } else {
                    newFaengers.add(newUser);
                }

            }
            //now update the playerMapping
            playerMapping.entrySet().stream().map(entry -> {
                if (currFaengers.contains(entry.getKey())) {
                    entry.getValue().setIsfaenger(true);
                    entry.getValue().setWhenFunged(System.currentTimeMillis());
                    return entry;
                } else {
                    entry.getValue().setIsfaenger(false);
                    entry.getValue().setWhenFunged(0L);
                    return entry;
                }
            });

        }


    }

    private List<User> getCurrFaenger(int lobby) {
        //computes the current faengers
        HashMap<User, Player> playerMapping = gameManager.getGameStates()[lobby].getPlayerMapping();
        List<User> faengers = playerMapping.entrySet().stream()
                .filter(entry -> entry.getValue().isIsfaenger())
                .map(entry -> entry.getKey()).collect(Collectors.toList());
        return faengers;
    }


}



