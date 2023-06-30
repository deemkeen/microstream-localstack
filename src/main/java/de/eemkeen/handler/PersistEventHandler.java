package de.eemkeen.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.eemkeen.model.BaseEvent;
import de.eemkeen.repo.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersistEventHandler implements MessageHandler<JsonNode> {

    private final ObjectMapper mapper;
    private final EventRepository eventRepository;

    public PersistEventHandler(EventRepository eventRepository, @Qualifier("nostrMapper") ObjectMapper mapper) {
        this.eventRepository = eventRepository;
        this.mapper = mapper;
    }

    @Override
    public void handle(JsonNode eventJson) {
        try {
            log.info("Incoming new event: {}", eventJson);
            BaseEvent baseEvent = mapper.treeToValue(eventJson, BaseEvent.class);
            if (eventRepository.getById(baseEvent.getId()).isEmpty()) {
                eventRepository.add(baseEvent);
                log.info("Persisting event with the id: {}", baseEvent.getId());
            }
        } catch (JsonProcessingException e) {
            log.error("Mapping error while persisting!", e);
        }

    }
}
