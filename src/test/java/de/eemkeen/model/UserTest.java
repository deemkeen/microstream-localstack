package de.eemkeen.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void builder_shouldCreateUserWithProvidedName() {
        // Arrange & Act
        String name = "Test User";
        User user = User.builder().name(name).build();

        // Assert
        assertEquals(name, user.getName());
        assertNotNull(user.getId());
    }

    @Test
    void builder_shouldCreateUserWithProvidedId() {
        // Arrange & Act
        String id = "custom-id";
        String name = "Test User";
        User user = User.builder().id(id).name(name).build();

        // Assert
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
    }

    @Test
    void builder_shouldGenerateUniqueIds() {
        // Arrange & Act
        User user1 = User.builder().name("User 1").build();
        User user2 = User.builder().name("User 2").build();

        // Assert
        assertNotEquals(user1.getId(), user2.getId());
    }

    @Test
    void equals_shouldReturnTrueForSameInstance() {
        // Arrange
        User user = User.builder().name("Test User").build();

        // Act & Assert
        assertEquals(user, user);
    }

    @Test
    void equals_shouldReturnTrueForEqualUsers() {
        // Arrange
        String id = "test-id";
        String name = "Test User";
        
        User user1 = User.builder().id(id).name(name).build();
        User user2 = User.builder().id(id).name(name).build();

        // Act & Assert
        assertEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentIds() {
        // Arrange
        String name = "Test User";
        
        User user1 = User.builder().id("id1").name(name).build();
        User user2 = User.builder().id("id2").name(name).build();

        // Act & Assert
        assertNotEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentTypes() {
        // Arrange
        User user = User.builder().name("Test User").build();
        Object other = new Object();

        // Act & Assert
        assertNotEquals(user, other);
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        // Arrange
        User user = User.builder().name("Test User").build();

        // Act & Assert
        assertNotEquals(user, null);
    }

    @Test
    void hashCode_shouldBeEqualForEqualUsers() {
        // Arrange
        String id = "test-id";
        String name = "Test User";
        
        User user1 = User.builder().id(id).name(name).build();
        User user2 = User.builder().id(id).name(name).build();

        // Act & Assert
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferentForDifferentUsers() {
        // Arrange
        User user1 = User.builder().id("id1").name("User 1").build();
        User user2 = User.builder().id("id2").name("User 2").build();

        // Act & Assert
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void toString_shouldContainUserDetails() {
        // Arrange
        String name = "Test User";
        User user = User.builder().name(name).build();

        // Act
        String result = user.toString();

        // Assert
        assertTrue(result.contains(name));
        assertTrue(result.contains(user.getId()));
    }
}