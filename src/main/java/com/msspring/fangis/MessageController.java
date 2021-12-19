package com.msspring.fangis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;

@Controller
public class MessageController {

    private Map<String, String> userMapping;
    private Game gameStatus;

    @Autowired
    public MessageController(Map<String, String> userStati, Game gameStatus) {
        this.userMapping = userStati;
        this.gameStatus = gameStatus;
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
