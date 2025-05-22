package de.eemkeen.model;

import one.microstream.storage.types.StorageManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RootTest {

    @Mock
    private StorageManager storageManager;

    private Root root;
    private User testUser;

    @BeforeEach
    void setUp() {
        root = new Root();
        ReflectionTestUtils.setField(root, "storageManager", storageManager);
        testUser = User.builder().name("Test User").build();
    }

    @Test
    void getUsers_shouldReturnEmptyListInitially() {
        // Act
        List<User> users = root.getUsers();

        // Assert
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void getUsers_shouldReturnCopyOfUsersList() {
        // Arrange
        root.addUser(testUser);

        // Act
        List<User> users = root.getUsers();
        users.clear(); // Modify the returned list

        // Assert
        assertEquals(1, root.getUsers().size()); // Original list should still have the user
    }

    @Test
    void addUser_shouldAddUserToList() {
        // Act
        User addedUser = root.addUser(testUser);

        // Assert
        assertEquals(testUser, addedUser);
        assertEquals(1, root.getUsers().size());
        assertEquals(testUser, root.getUsers().get(0));
        verify(storageManager).store(root.getUsers());
    }

    @Test
    void updateUser_shouldStoreUser() {
        // Arrange
        root.addUser(testUser);

        // Act
        root.updateUser(testUser);

        // Assert
        verify(storageManager).store(testUser);
    }

    @Test
    void removeUser_shouldRemoveUserFromList() {
        // Arrange
        root.addUser(testUser);
        assertEquals(1, root.getUsers().size());
        // Reset the mock to clear the first store call from addUser
        reset(storageManager);

        // Act
        root.removeUser(testUser);

        // Assert
        assertTrue(root.getUsers().isEmpty());
        verify(storageManager).store(root.getUsers());
    }
}