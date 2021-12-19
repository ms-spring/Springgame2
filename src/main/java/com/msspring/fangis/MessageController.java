package com.msspring.fangis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class MessageController {

    private Map<String, String> userMapping;
    private GameManager gameManager;


    @Autowired
    public MessageController(Map<String, String> userStati, GameManager gameManager) {
        this.userMapping = userStati;
        this.gameManager = gameManager;

    }




    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting addUser(@Header("simpSessionId") String sessionId, UserNameMessage message) throws Exception {
        

        return new Greeting();
    }

   @MessageMapping("/update")
   @SendTo("/game/broadcast")
   public void updateUser(@Header("simpSessionId") String sessionId, StatusMessage message) throws Exception {



        return;
   }



}
