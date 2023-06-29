package de.eemkeen;

import de.eemkeen.websocket.SendEndPoint;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.microstream.integrations.spring.boot.types.MicroStreamConfiguration;
import org.eclipse.jetty.websocket.api.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@SpringBootApplication(exclude = MicroStreamConfiguration.class)
@Slf4j
public class MicrostreamApplication {

  private final Session session;
  private final SendEndPoint sendEndPoint;

  private final int sessionId = (new Random().nextInt(99) + 100);

  public static void main(String[] args) {
    SpringApplication.run(MicrostreamApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void getEventsOnStartup() {
    sendEndPoint.onText(session, "[\"REQ\", \"" + sessionId + "\", {\"kinds\":[0,1,42]}]");
  }
}
