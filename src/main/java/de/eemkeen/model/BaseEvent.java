package de.eemkeen.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class BaseEvent {
  String id;
  long createdAt;
  int kind;
  String content;
  String sig;
}
