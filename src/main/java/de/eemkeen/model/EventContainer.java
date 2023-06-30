package de.eemkeen.model;

import lombok.Value;
import one.microstream.reference.Lazy;

import java.util.HashSet;
import java.util.Set;

@Value
public class EventContainer {
    Lazy<Set<BaseEvent>> events = Lazy.Reference(new HashSet<>());
}
