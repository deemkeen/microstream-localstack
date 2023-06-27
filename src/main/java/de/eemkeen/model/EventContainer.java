package de.eemkeen.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Value;
import one.microstream.reference.Lazy;

@Value
public class EventContainer {
  Lazy<Set<BaseEvent>> events = Lazy.Reference(new HashSet<>());
}
