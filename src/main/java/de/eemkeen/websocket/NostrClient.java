package de.eemkeen.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.eemkeen.handler.MessageType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class NostrClient {
    private final Session session;
    private final ObjectMapper mapper;
    private final SessionInfo sessionInfo;

    public void subscribe(@NonNull NostrQuery find) throws IOException {
        try {
            String findJson = mapper.writeValueAsString(find);
            String query = String.format("[\"%s\", \"%s\", %s]", MessageType.REQ.name(), sessionInfo.getSessionId(), findJson);
            session.getRemote().sendString(query);
        } catch (
                JsonProcessingException e) {
            log.error("Mapping error while sending query!", e);
        }
    }

    public void stopSubscription() throws IOException {
        String query = String.format("[\"%s\", \"%s\"]", MessageType.CLOSE.name(), sessionInfo.getSessionId());
        sessionInfo.setNewEvents(false);
        session.getRemote().sendString(query);
    }
}
