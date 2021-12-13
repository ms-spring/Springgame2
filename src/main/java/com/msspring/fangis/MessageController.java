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

    private Map<String, String> userStati;

    @Autowired
    public MessageController(Map<String, String> userStati) {
        this.userStati = userStati;
    }




    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(@Header("simpSessionId") String sessionId, UserNameMessage message) throws Exception {


        userStati.put(message.getName(), "fett");

        for (Map.Entry entry : userStati.entrySet()) {
            System.out.println(entry.getKey());
        }

        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

   @MessageMapping("/update")
   @SendTo("/game/broadcast")
   public void updateUser(@Header("simpSessionId") String sessionId, StatusMessage message) throws Exception {



        return;
   }



}
