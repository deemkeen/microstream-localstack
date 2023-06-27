package de.eemkeen.model;

import lombok.*;

@Value
@NoArgsConstructor(force = true)
public class BaseEvent {
  String id;
  long createdAt;
  int kind;
  String content;
  String pubkey;
  String sig;
}
