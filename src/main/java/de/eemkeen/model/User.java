package de.eemkeen.model;

import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class User {
  @Builder.Default private final String id = UUID.randomUUID().toString();
  private final String name;
}
