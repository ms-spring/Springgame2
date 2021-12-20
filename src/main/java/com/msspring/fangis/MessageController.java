package com.msspring.fangis;

import com.msspring.fangis.exceptions.InvalidSessionException;
import com.msspring.fangis.exceptions.InvalidStatusMessageException;
import com.msspring.fangis.exceptions.InvalidUserNameMessageException;
import com.msspring.fangis.exceptions.UserNameAlreadyUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Map;

@Controller
public class MessageController {
    private static final Validator validator;

    static {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        try (ValidatorFactory factory = config.buildValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private Map<String, User> userMapping;
    private GameManager gameManager;


    @Autowired
    public MessageController(Map<String, User> userMapping, GameManager gameManager) {
        this.userMapping = userMapping;
        this.gameManager = gameManager;

    }


    @MessageMapping("/hello")
    public void login(@Header("simpSessionId") String sessionId, UserNameMessage message) throws Exception {
        if (!validator.validate(message).isEmpty()) {
            throw new InvalidUserNameMessageException();
        }

        String username = message.getName();
        //check if username already taken!
        if (userMapping.values().stream().anyMatch(user -> user.getName().equals(username))) {
            throw new UserNameAlreadyUsedException();
        }
        int lobby = message.getLobby();
        if (0 < lobby && lobby >= gameManager.getGameStates().length) {
            throw new IndexOutOfBoundsException();
        }

        User user = new User(username, message.getLobby());
        userMapping.put(sessionId, user);


    }


    @MessageMapping("/update")
    @SendTo("/game/broadcast")
    public void updateUser(@Header("simpSessionId") String sessionId, StatusMessage message) throws InvalidSessionException, InvalidStatusMessageException {
        if (!userMapping.containsKey(sessionId)) {
            throw new InvalidSessionException();
        }
        if (!validator.validate(message).isEmpty()) {
            throw new InvalidStatusMessageException();
        }
        User user = userMapping.get(sessionId);
        GameState state = gameManager.getGameStates()[0];
        Player player = state.getPlayerMapping().get(user);
        player.setPosition(message.getPosition());
    }
}
