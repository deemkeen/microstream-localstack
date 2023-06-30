package de.eemkeen.websocket;

import de.eemkeen.handler.ReadMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.WebSocketPingPongListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientEndPoint extends WebSocketAdapter implements WebSocketPingPongListener {

    private final ReadMessageHandler readMessageHandler;
    private final CountDownLatch closureLatch = new CountDownLatch(1);

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        log.info("Endpoint connected: {}", sess);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        readMessageHandler.handle(message);
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
            log.info("Received a ping from server, sending pong back!");
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
