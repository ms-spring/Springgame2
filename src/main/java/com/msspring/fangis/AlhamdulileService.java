package com.msspring.fangis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

// 5/5 Sterne - Super Service!
@Component
public class AlhamdulileService {
    private Map<String, User> userMapping;
    private GameManager gameManager;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AlhamdulileService(Map<String, User> userMapping, GameManager gameManager, SimpMessagingTemplate messagingTemplate) {
        this.userMapping = userMapping;
        this.gameManager = gameManager;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate=500)
    public void update() {
        System.out.println(this.userMapping);
        messagingTemplate.convertAndSend("/topic/greetings", new Greeting("alhamdulile" + Math.random()));
    }
}



