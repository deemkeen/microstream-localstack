package de.eemkeen;

import com.github.javafaker.Faker;
import de.eemkeen.model.User;
import de.eemkeen.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import one.microstream.integrations.spring.boot.types.MicroStreamConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@SpringBootApplication(exclude = MicroStreamConfiguration.class)
public class MicrostreamApplication {

  private final UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(MicrostreamApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void ListUserOnStartup() {
    userRepository.add(User.builder().name(Faker.instance().funnyName().name()).build());
    printUsers();
  }

  private void printUsers() {
    userRepository.getAll().forEach(System.out::println);
  }
}
