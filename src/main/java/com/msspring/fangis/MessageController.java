package com.msspring.fangis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class MessageController {

    private Map<String, User> userMapping;
    private GameManager gameManager;


    @Autowired
    public MessageController(Map<String, User> userStati, GameManager gameManager) {
        this.userMapping = userStati;
        this.gameManager = gameManager;

    }


    @MessageMapping("/hello")
    public void login(@Header("simpSessionId") String sessionId, UserNameMessage message) throws Exception {
        System.out.println(this.gameManager.getGameStates()[0]);
        return;
    }

    @MessageMapping("/update")
    @SendTo("/game/broadcast")
    public void updateUser(@Header("simpSessionId") String sessionId, StatusMessage message) throws Exception {



        return;
    }



}
