package de.eemkeen;

import lombok.extern.slf4j.Slf4j;
import one.microstream.integrations.spring.boot.types.MicroStreamConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = MicroStreamConfiguration.class)
@Slf4j
public class MicrostreamApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicrostreamApplication.class, args);
    }
}
