package de.eemkeen.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.eemkeen.websocket.SessionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReadMessageHandler implements MessageHandler<String> {

    private final MessageHandler<JsonNode> messageHandler;
    private final SessionInfo sessionInfo;

    private final ObjectMapper mapper;


    public ReadMessageHandler(MessageHandler<JsonNode> messageHandler, SessionInfo sessionInfo, @Qualifier("nostrMapper") ObjectMapper mapper) {
        this.messageHandler = messageHandler;
        this.sessionInfo = sessionInfo;
        this.mapper = mapper;
    }

    @Override
    public void handle(String message) {

        try {

            JsonNode jsonNode = mapper.readTree(message);
            MessageType event = MessageType.valueOf(jsonNode.get(0).textValue());

            switch (event) {
                case REQ, CLOSE -> log.warn("This is a client event, skipping! : {}", event);
                case EOSE -> sessionInfo.setNewEvents(true);
                case EVENT -> {
                    if (sessionInfo.isNewEvents()) messageHandler.handle(jsonNode.get(2));
                }
                case NOTICE -> log.info("Relay message: {}", jsonNode.get(1).textValue());
            }
        } catch (
                JsonProcessingException e) {
            log.error("Mapping error!", e);
        }
    }
}
