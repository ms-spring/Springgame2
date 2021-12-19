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
    private GameManager gameManagerState;
    private testBeans testbean;

    @Autowired
    public MessageController(Map<String, String> userStati, GameManager gameManagerState, testBeans testbean) {
        this.userMapping = userStati;
        this.gameManagerState = gameManagerState;
        this.testbean = testbean;
    }




    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting addUser(@Header("simpSessionId") String sessionId, UserNameMessage message) throws Exception {
        gameManagerState.gameStates[0].getUserStates().put("dini mueter", null);
        boolean test = testbean.getGameState().gameStates[0].getUserStates().isEmpty();
        if (test) {
            return new Greeting("ababababab");
        }

        return new Greeting();
    }

   @MessageMapping("/update")
   @SendTo("/game/broadcast")
   public void updateUser(@Header("simpSessionId") String sessionId, StatusMessage message) throws Exception {



        return;
   }



}
