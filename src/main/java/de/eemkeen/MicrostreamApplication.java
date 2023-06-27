package de.eemkeen;

import de.eemkeen.repo.EventRepository;
import de.eemkeen.websocket.ClientEndPoint;
import de.eemkeen.websocket.SendEndPoint;
import java.net.URI;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.microstream.integrations.spring.boot.types.MicroStreamConfiguration;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@SpringBootApplication(exclude = MicroStreamConfiguration.class)
@Slf4j
public class MicrostreamApplication {

  private final ClientEndPoint clientEndPoint;

  private final EventRepository eventRepository;
  private final int sessionId = (new Random().nextInt(99) + 100);

  public static void main(String[] args) {
    SpringApplication.run(MicrostreamApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void getEventsOnStartup() throws Exception {

    int size = eventRepository.getAll().size();
    log.info("Collection size: {}", size);

    // Use a standard, HTTP/1.1, HttpClient.
    HttpClient httpClient = new HttpClient();
    httpClient.setIdleTimeout(0);

    // Create and start WebSocketClient.
    WebSocketClient webSocketClient = new WebSocketClient(httpClient);
    webSocketClient.setIdleTimeout(Duration.ZERO);
    webSocketClient.start();

    // The client-side WebSocket EndPoint that
    // receives WebSocket messages from the server.

    // The server URI to connect to.
    URI serverURI = URI.create("wss://nos.lol");

    // Connect the client EndPoint to the server.
    CompletableFuture<Session> clientSessionPromise =
        webSocketClient.connect(clientEndPoint, serverURI);
    SendEndPoint sendEndPoint = new SendEndPoint();
    clientSessionPromise.whenComplete(
        (Session s, Throwable t) ->
            sendEndPoint.onText(s, "[\"REQ\", \"" + sessionId + "\", {\"kinds\":[0,1,42]}]"));
  }
}
