package de.eemkeen.websocket;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
@Slf4j
public class SendEndPoint {
  @OnWebSocketMessage
  public void onText(Session session, String text) {
    // Obtain the RemoteEndpoint APIs.
    RemoteEndpoint remote = session.getRemote();

    try {
      // Send textual data to the remote peer.
      remote.sendString(text);
    } catch (IOException x) {
      // No need to rethrow or close the session.
      log.warn("could not send data", x);
    }
  }
}
