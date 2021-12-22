package com.msspring.fangis;

import com.msspring.fangis.exceptions.InvalidSessionException;
import com.msspring.fangis.exceptions.InvalidStatusMessageException;
import com.msspring.fangis.exceptions.InvalidUserNameMessageException;
import com.msspring.fangis.exceptions.InvalidUserNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Map;
import java.util.Random;

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
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageController(Map<String, User> userMapping, GameManager gameManager, SimpMessagingTemplate messagingTemplate) {
        this.userMapping = userMapping;
        this.gameManager = gameManager;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void onConnectEvent(SessionConnectEvent e) {
        //System.out.println(e);
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent e) throws InvalidSessionException {
        String sessionId = e.getSessionId();
        if(!userMapping.containsKey(sessionId)) {
            throw new InvalidSessionException();
        }
        User user = userMapping.get(sessionId);
        userMapping.remove(sessionId);
        gameManager.getGameStates()[user.getLobby()].getPlayerMapping().remove(user);
    }

    @MessageMapping("/hello")
    public void login(@Header("simpSessionId") String sessionId, UserNameMessage message) throws Exception {
        if (!validator.validate(message).isEmpty()) {
            throw new InvalidUserNameMessageException();
        }

        String username = message.getName();

        //check if username already taken!
        if (userMapping.values().stream().anyMatch(user -> user.getName().equals(username))) {
            throw new InvalidUserNameException();
        }
        int lobby = message.getLobby();
        if (0 < lobby && lobby >= gameManager.getGameStates().length) {
            throw new IndexOutOfBoundsException();
        }

        User user = new User(username, message.getLobby());
        userMapping.put(sessionId, user);
        //800 uf x 600 uf y

        Random r = new Random();
        gameManager.getGameStates()[0].getPlayerMapping().put(user, new Player(new Position(r.nextInt(800),r.nextInt(600))));

        return;
    }


    @MessageMapping("/update")
    public void updateUser(@Header("simpSessionId") String sessionId, StatusMessage message) throws InvalidSessionException, InvalidStatusMessageException {
        // Check for valid update
        if (!userMapping.containsKey(sessionId)) {
            throw new InvalidSessionException();
        }
        if (!validator.validate(message).isEmpty()) {
            throw new InvalidStatusMessageException();
        }

        // Update the player state
        User user = userMapping.get(sessionId);
        GameState state = gameManager.getGameStates()[user.getLobby()];
        Player player = state.getPlayerMapping().get(user);
        player.setPosition(message.getPosition());
        player.setMove(message.getMove());
    }
}
