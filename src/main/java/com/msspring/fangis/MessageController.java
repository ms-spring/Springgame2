package com.msspring.fangis;

import com.msspring.fangis.exceptions.InvalidSessionException;
import com.msspring.fangis.exceptions.InvalidStatusMessageException;
import com.msspring.fangis.exceptions.InvalidUserNameMessageException;
import com.msspring.fangis.exceptions.UserNameAlreadyUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

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
    @SendTo("/topic/greetings")
    public Player login(@Header("simpSessionId") String sessionId, UserNameMessage message) throws Exception {
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

        return new Player(new Position(5, 5));
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

        /*
        // Prepare response message
        return new GameStateMessage(
                state.getPlayerMapping().entrySet().stream().map(
                        e -> new PlayerState(e.getKey(), e.getValue())).toArray(PlayerState[]::new));

         */
    }
}
