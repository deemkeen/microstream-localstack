package de.eemkeen.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.eemkeen.model.BaseEvent;
import de.eemkeen.repo.EventRepository;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientEndPoint extends WebSocketAdapter implements WebSocketPingPongListener {
  private final CountDownLatch closureLatch = new CountDownLatch(1);

  private final ObjectMapper mapper = new ObjectMapper();
  private final EventRepository eventRepository;

  public ClientEndPoint(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
    this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Override
  public void onWebSocketConnect(Session sess) {
    super.onWebSocketConnect(sess);
    log.info("Endpoint connected: {}", sess);
  }

  @Override
  public void onWebSocketText(String message) {
    super.onWebSocketText(message);
    log.info("Received TEXT message: {}", message);

    if (message.contains("\"EVENT\"")) {
      try {
        JsonNode jsonNode = mapper.readTree(message);
        JsonNode baseEventNode = jsonNode.get(2);
        BaseEvent baseEvent = mapper.treeToValue(baseEventNode, BaseEvent.class);
        log.info(String.valueOf(baseEvent));
        eventRepository.add(baseEvent);
      } catch (JsonProcessingException e) {
        log.error("Mapping error!", e);
      }
    }

    if ("close".equalsIgnoreCase(message)) {
      onWebSocketClose(StatusCode.NORMAL, "bye");
    }
  }

  @Override
  public void onWebSocketClose(int statusCode, String reason) {
    super.onWebSocketClose(statusCode, reason);
    log.info("Socket Closed: [{}] {}", statusCode, reason);
    closureLatch.countDown();
  }

  @Override
  public void onWebSocketError(Throwable cause) {
    super.onWebSocketError(cause);
    cause.printStackTrace();
  }

  private static long getRoundTrip(ByteBuffer payload) {
    try {
      long start = payload.getLong();
      long roundTrip = System.nanoTime() - start;
      return TimeUnit.MILLISECONDS.convert(roundTrip, TimeUnit.NANOSECONDS);
    } catch (Exception e) {
      return 0L;
    }
  }

  @Override
  public void onWebSocketPing(ByteBuffer payload) {
    try {
      log.info("Recieved a ping from server, sending pong back!");
      getRemote().sendPong(payload);
    } catch (IOException e) {
      log.error("An exception occurred while processing a ping message : {}", e.getMessage());
    }
  }

  @Override
  public void onWebSocketPong(ByteBuffer payload) {
    log.info(
        "Received a pong with a roundtrip of {} milliseconds",
        TimeUnit.MILLISECONDS.convert(getRoundTrip(payload), TimeUnit.NANOSECONDS));
  }
}
