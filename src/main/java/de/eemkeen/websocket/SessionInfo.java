package de.eemkeen.websocket;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Random;

@Data
@Component
public class SessionInfo {
    Integer sessionId = (new Random().nextInt(99) + 100);
    private boolean newEvents;
}
