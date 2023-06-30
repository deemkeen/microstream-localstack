package de.eemkeen.websocket;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.time.Duration;

@Configuration
public class SessionConfiguration {

    @Value("${nostr.relay.address}")
    private String nostrRelay;

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
        URI serverURI = URI.create(nostrRelay);

        // Connect the client EndPoint to the server.
        return webSocketClient.connect(clientEndPoint, serverURI).get();
    }

    // ObjectMapper Config
    @Bean("nostrMapper")
    public ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
