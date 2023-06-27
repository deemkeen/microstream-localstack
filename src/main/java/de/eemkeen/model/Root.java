package de.eemkeen.model;

import java.util.HashSet;
import java.util.Set;
import one.microstream.integrations.spring.boot.types.Storage;
import one.microstream.reference.Lazy;
import one.microstream.storage.types.StorageManager;
import org.springframework.beans.factory.annotation.Autowired;

@Storage
public class Root {

  @Autowired private transient StorageManager storageManager;

  private final EventContainer eventContainer = new EventContainer();

  public Set<BaseEvent> getEvents() {
    return new HashSet<>(Lazy.get(eventContainer.getEvents()));
  }

  public EventContainer getContainer(){
    return eventContainer;
  }

  public BaseEvent addEvent(final BaseEvent event) {
    Set<BaseEvent> baseEvents = Lazy.get(eventContainer.getEvents());
    baseEvents.add(event);
    storageManager.store(baseEvents);
    return event;
  }

  public void removeEvent(final BaseEvent event) {
    Set<BaseEvent> baseEvents = Lazy.get(eventContainer.getEvents());
    baseEvents.remove(event);
    storageManager.store(baseEvents);
  }
}
