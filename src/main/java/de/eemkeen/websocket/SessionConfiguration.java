package de.eemkeen.websocket;

import java.net.URI;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SessionConfiguration {

  private final ClientEndPoint clientEndPoint;

  @Bean
  public Session getSession(ClientEndPoint clientEndPoint) throws Exception {
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
    return webSocketClient.connect(clientEndPoint, serverURI).get();
  }
}
