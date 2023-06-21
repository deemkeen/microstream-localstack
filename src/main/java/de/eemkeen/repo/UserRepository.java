package de.eemkeen.repo;

import java.util.List;
import java.util.Optional;

import de.eemkeen.exception.UserAlreadyExistsException;
import de.eemkeen.exception.UserNotFoundException;
import de.eemkeen.model.Root;
import de.eemkeen.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final Root root;

  public List<User> getAll() {
    return root.getUsers();
  }

  public Optional<User> getById(String id) {
    return root.getUsers().stream().filter(u -> u.getId().equals(id)).findAny();
  }

  public User add(User user) {
    User result;

    Optional<User> byId = getById(user.getId());
    if (byId.isPresent()) {
      throw new UserAlreadyExistsException();
    }
    result = root.addUser(User.builder().name(user.getName()).build());
    return result;
  }

  public User updateName(String id, String name) {
    Optional<User> byId = getById(id);
    if (byId.isEmpty()) {
      throw new UserNotFoundException();
    }

    User updatedUser = User.builder().id(id).name(name).build();
    root.updateUser(updatedUser);
    return updatedUser;
  }

  public void removeById(String id){
    getById(id).ifPresent(root::removeUser);
  }

  
}
