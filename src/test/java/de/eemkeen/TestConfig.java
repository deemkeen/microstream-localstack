package de.eemkeen;

import de.eemkeen.model.Root;
import de.eemkeen.repo.UserRepository;
import one.microstream.afs.blobstore.types.BlobStoreFileSystem;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.services.s3.S3Client;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public S3Client s3Client() {
        return Mockito.mock(S3Client.class);
    }

    @Bean
    @Primary
    public BlobStoreFileSystem blobStoreFileSystem() {
        return Mockito.mock(BlobStoreFileSystem.class);
    }

    @Bean
    @Primary
    public EmbeddedStorageManager embeddedStorageManager() {
        return Mockito.mock(EmbeddedStorageManager.class);
    }

    @Bean
    @Primary
    public Root root() {
        return Mockito.mock(Root.class);
    }

    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }
}