package de.eemkeen.model;

import one.microstream.persistence.types.Storer;
import one.microstream.storage.types.StorageManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InitializerTest {

    @Mock
    private StorageManager storageManager;

    @Mock
    private Root root;

    @Mock
    private Storer storer;

    private Initializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new Initializer();
        when(storageManager.root()).thenReturn(root);
    }

    @Test
    void initialize_shouldStoreRootWhenUsersEmpty() {
        // Arrange
        when(root.getUsers()).thenReturn(new ArrayList<>());
        when(storageManager.createEagerStorer()).thenReturn(storer);
        
        // Act
        initializer.initialize(storageManager);

        // Assert
        verify(storageManager).createEagerStorer();
        verify(storer).store(root);
    }
    
    @Test
    void initialize_shouldNotStoreRootWhenUsersNotEmpty() {
        // Arrange
        ArrayList<User> users = new ArrayList<>();
        users.add(User.builder().name("Existing User").build());
        when(root.getUsers()).thenReturn(users);
        
        // Act
        initializer.initialize(storageManager);
        
        // Assert
        verify(storageManager, never()).createEagerStorer();
    }
}