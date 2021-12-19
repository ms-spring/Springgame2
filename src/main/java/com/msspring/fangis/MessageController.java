package com.msspring.fangis;

import com.msspring.fangis.exceptions.InvalidSessionException;
import com.msspring.fangis.exceptions.UserNameAlreadyUsedException;
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
        String username = message.getName();
        //check if username already taken!
        if (userMapping.values().stream().anyMatch(p -> p.getName().equals(username))) {
            throw new UserNameAlreadyUsedException();
        }
        int lobby = message.getLobby();
        if (!(0 <= lobby && lobby <= 2)) {
            throw new IndexOutOfBoundsException();
        }

        User user = new User(username, message.getLobby());
        userMapping.put(sessionId, user);


        return;
    }

    @MessageMapping("/update")
    @SendTo("/game/broadcast")
    public void updateUser(@Header("simpSessionId") String sessionId, StatusMessage message) throws RuntimeException, InvalidSessionException {
        if(sessionId.contains("KAKA DIE BOHNE")) {
            System.out.println("KAKA DIE SAFTI BOHNE KAKA");
        }

        throw new InvalidSessionException();
    }


}
