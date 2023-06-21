package de.eemkeen;

import de.eemkeen.model.User;
import de.eemkeen.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@RequiredArgsConstructor
public class MicrostreamApplication {

    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(MicrostreamApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ListUserOnStartup() {

        printUsers();
        // add new
        userRepository.add(User.builder().name("New User!").build());
        printUsers();

    }

    private void printUsers() {
        userRepository.getAll().
                forEach(System.out::println);
    }

}
