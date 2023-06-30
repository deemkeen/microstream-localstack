package de.eemkeen.controller;

import de.eemkeen.websocket.NostrClient;
import de.eemkeen.websocket.NostrQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class NostrSubscriptionController {

    private final NostrClient nostrClient;

    @PostMapping(value = "/subscribe")
    public ResponseEntity<Void> subscribe(@RequestBody NostrQuery query) {
        try {
            nostrClient.subscribe(query);
        } catch (IOException e) {
            ResponseEntity.badRequest();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/unsubscribe")
    public ResponseEntity<Void> unsubscribe() {
        try {
            nostrClient.stopSubscription();
        } catch (IOException e) {
            ResponseEntity.badRequest();
        }
        return ResponseEntity.ok().build();
    }

}
