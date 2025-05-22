package de.eemkeen.repo;

import de.eemkeen.exception.UserAlreadyExistsException;
import de.eemkeen.exception.UserNotFoundException;
import de.eemkeen.model.Root;
import de.eemkeen.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private Root root;

    private UserRepository userRepository;
    private User testUser;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(root);
        testUser = User.builder().name("Test User").build();
        userList = new ArrayList<>();
        userList.add(testUser);
    }

    @Test
    void getAll_shouldReturnAllUsers() {
        // Arrange
        when(root.getUsers()).thenReturn(userList);

        // Act
        List<User> result = userRepository.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(root).getUsers();
    }

    @Test
    void getById_shouldReturnUserWhenExists() {
        // Arrange
        String userId = testUser.getId();
        when(root.getUsers()).thenReturn(userList);

        // Act
        Optional<User> result = userRepository.getById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(root).getUsers();
    }

    @Test
    void getById_shouldReturnEmptyWhenUserDoesNotExist() {
        // Arrange
        String nonExistentId = "non-existent-id";
        when(root.getUsers()).thenReturn(userList);

        // Act
        Optional<User> result = userRepository.getById(nonExistentId);

        // Assert
        assertTrue(result.isEmpty());
        verify(root).getUsers();
    }

    @Test
    void add_shouldAddUserSuccessfully() {
        // Arrange
        User newUser = User.builder().name("New User").build();
        when(root.getUsers()).thenReturn(userList);
        when(root.addUser(any(User.class))).thenReturn(newUser);

        // Act
        User result = userRepository.add(newUser);

        // Assert
        assertEquals(newUser, result);
        verify(root).getUsers();
        verify(root).addUser(any(User.class));
    }

    @Test
    void add_shouldThrowExceptionWhenUserAlreadyExists() {
        // Arrange
        when(root.getUsers()).thenReturn(userList);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userRepository.add(testUser));
        verify(root).getUsers();
        verify(root, never()).addUser(any(User.class));
    }

    @Test
    void updateName_shouldUpdateUserNameSuccessfully() {
        // Arrange
        String userId = testUser.getId();
        String newName = "Updated Name";
        when(root.getUsers()).thenReturn(userList);

        // Act
        User result = userRepository.updateName(userId, newName);

        // Assert
        assertEquals(userId, result.getId());
        assertEquals(newName, result.getName());
        verify(root).getUsers();
        verify(root).updateUser(any(User.class));
    }

    @Test
    void updateName_shouldThrowExceptionWhenUserDoesNotExist() {
        // Arrange
        String nonExistentId = "non-existent-id";
        String newName = "Updated Name";
        when(root.getUsers()).thenReturn(userList);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userRepository.updateName(nonExistentId, newName));
        verify(root).getUsers();
        verify(root, never()).updateUser(any(User.class));
    }

    @Test
    void removeById_shouldRemoveUserWhenExists() {
        // Arrange
        String userId = testUser.getId();
        when(root.getUsers()).thenReturn(userList);

        // Act
        userRepository.removeById(userId);

        // Assert
        verify(root).getUsers();
        verify(root).removeUser(testUser);
    }

    @Test
    void removeById_shouldDoNothingWhenUserDoesNotExist() {
        // Arrange
        String nonExistentId = "non-existent-id";
        when(root.getUsers()).thenReturn(userList);

        // Act
        userRepository.removeById(nonExistentId);

        // Assert
        verify(root).getUsers();
        verify(root, never()).removeUser(any(User.class));
    }
}