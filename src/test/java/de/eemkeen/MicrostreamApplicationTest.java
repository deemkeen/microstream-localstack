package de.eemkeen;

import de.eemkeen.model.User;
import de.eemkeen.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MicrostreamApplicationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MicrostreamApplication microstreamApplication;

    @Test
    void listUserOnStartup_shouldAddUserAndPrintUsers() {
        // Arrange
        User user = User.builder().name("Test User").build();
        when(userRepository.add(any(User.class))).thenReturn(user);
        when(userRepository.getAll()).thenReturn(Collections.singletonList(user));

        // Act
        microstreamApplication.listUserOnStartup();

        // Assert
        verify(userRepository).add(any(User.class));
        verify(userRepository).getAll();
    }

    @Test
    void main_shouldRunSpringApplication() {
        // Use PowerMockito to mock the static method
        try (var springAppMock = mockStatic(SpringApplication.class)) {
            // Act
            String[] args = new String[0];
            MicrostreamApplication.main(args);
            
            // Assert
            springAppMock.verify(() -> 
                SpringApplication.run(eq(MicrostreamApplication.class), eq(args))
            );
        }
    }
}