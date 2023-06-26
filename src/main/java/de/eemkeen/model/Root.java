package de.eemkeen.model;

import java.util.ArrayList;
import java.util.List;
import one.microstream.integrations.spring.boot.types.Storage;
import one.microstream.storage.types.StorageManager;
import org.springframework.beans.factory.annotation.Autowired;

@Storage
public class Root {

  @Autowired private transient StorageManager storageManager;

  private final List<User> users = new ArrayList<>();

  public List<User> getUsers() {
    return new ArrayList<>(users);
  }

  public User addUser(User user) {
    users.add(user);
    storageManager.store(users);
    return user;
  }

  /**
   * Since the User instance is already part of the User Collection, we just need to make it is
   * stored externally.
   *
   * @param user
   */
  public void updateUser(User user) {
    storageManager.store(user);
  }

  public void removeUser(User user) {
    users.remove(user);
    storageManager.store(users);
  }
}
