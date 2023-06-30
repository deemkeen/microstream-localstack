package de.eemkeen.model;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@NoArgsConstructor(force = true)
public class BaseEvent {
    String id;
    long createdAt;
    int kind;
    List<List<String>> tags;
    String content;
    String pubkey;
    String sig;
}
