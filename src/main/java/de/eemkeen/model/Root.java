package de.eemkeen.model;

import java.util.HashSet;
import java.util.Set;

import one.microstream.integrations.spring.boot.types.Storage;
import one.microstream.storage.types.StorageManager;
import org.springframework.beans.factory.annotation.Autowired;

@Storage
public class Root {

  @Autowired private transient StorageManager storageManager;

  private final Set<BaseEvent> events = new HashSet<>();

  public Set<BaseEvent> getEvents() {
    return new HashSet<>(events);
  }

  public BaseEvent addEvent(BaseEvent event) {
    events.add(event);
    storageManager.store(events);
    return event;
  }

  /**
   * Since the BaseEvent instance is already part of the BaseEvent Collection, we just need to make
   * it is stored externally.
   *
   * @param event
   */
  public void updateEvent(BaseEvent event) {
    storageManager.store(event);
  }

  public void removeEvent(BaseEvent event) {
    events.remove(event);
    storageManager.store(events);
  }
}
