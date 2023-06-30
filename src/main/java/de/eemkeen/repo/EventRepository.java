package de.eemkeen.repo;

import de.eemkeen.model.BaseEvent;
import de.eemkeen.model.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EventRepository {

    private final Root root;

    public Set<BaseEvent> getAll() {
        return root.getEvents();
    }

    public Optional<BaseEvent> getById(final String id) {
        return root.getEvents().stream().filter(e -> e.getId().equals(id)).findAny();
    }

    public Optional<BaseEvent> getByPubkey(final String pubkey) {
        return root.getEvents().stream().filter(e -> e.getPubkey().equals(pubkey)).findAny();
    }

    public BaseEvent add(final BaseEvent event) {
        BaseEvent result;
        Optional<BaseEvent> byId = getById(event.getId());
        if (byId.isEmpty()) {
            result = root.addEvent(event);
        } else {
            log.debug("Event already stored: {}", event.getId());
            result = event;
        }
        return result;
    }

    public void removeById(final String id) {
        getById(id).ifPresent(root::removeEvent);
    }

    public int size() {
        return root.getEvents().size();
    }
}
