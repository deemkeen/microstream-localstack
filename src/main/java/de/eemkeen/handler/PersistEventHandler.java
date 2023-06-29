package de.eemkeen.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.eemkeen.model.BaseEvent;
import de.eemkeen.repo.EventRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersistEventHandler implements MessageHandler {

  private final ObjectMapper mapper = new ObjectMapper();
  private final EventRepository eventRepository;

  public PersistEventHandler(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
    this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Override
  public void handle(String message) {
    try {
      JsonNode jsonNode = mapper.readTree(message);
      JsonNode baseEventNode = jsonNode.get(2);
      BaseEvent baseEvent = mapper.treeToValue(baseEventNode, BaseEvent.class);
      if (eventRepository.getById(baseEvent.getId()).isEmpty()) {
        eventRepository.add(baseEvent);
        log.info("Persisting event with the id: {}", baseEvent.getId());
      }
    } catch (JsonProcessingException e) {
      log.error("Mapping error!", e);
    }
  }
}
