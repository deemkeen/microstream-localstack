package de.eemkeen.configuration;

import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import one.microstream.afs.aws.s3.types.S3Connector;
import one.microstream.afs.blobstore.types.BlobStoreFileSystem;
import one.microstream.integrations.spring.boot.types.MicroStreamConfiguration;
import one.microstream.integrations.spring.boot.types.config.MicrostreamConfigurationProperties;
import one.microstream.integrations.spring.boot.types.config.StorageManagerFactory;
import one.microstream.reflect.ClassLoaderProvider;
import one.microstream.storage.embedded.types.EmbeddedStorageFoundation;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;
import one.microstream.storage.types.Storage;
import one.microstream.storage.types.StorageChannelCountProvider;
import one.microstream.storage.types.StorageConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MicrostreamConfigurationProperties.class)
@ComponentScan(
    basePackages = "one.microstream.integrations.spring.boot.types",
    excludeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          value = MicroStreamConfiguration.class),
      @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = StorageManagerFactory.class),
    })
public class Config {

  private final MicrostreamConfigurationProperties configurationProperties;

  @Bean
  public S3Client getS3Client() {
    try {
      return LocalStackUtils.s3Client(
          configurationProperties.getStorageFilesystem().getAws(),
          configurationProperties.getStorageDirectory());
    } catch (URISyntaxException e) {
      return null;
    }
  }

  @Bean
  public BlobStoreFileSystem getBlobStorage(S3Client s3Client) {
    return BlobStoreFileSystem.New(S3Connector.Caching(s3Client));
  }

  @Bean("internal")
  public EmbeddedStorageManager getStorageManager(BlobStoreFileSystem fileSystem) {
    final var foundation =
        EmbeddedStorageFoundation.New()
            .setConfiguration(
                StorageConfiguration.Builder()
                    .setStorageFileProvider(
                        Storage.FileProviderBuilder(fileSystem)
                            .setDirectory(
                                fileSystem.ensureDirectoryPath(
                                    configurationProperties.getStorageDirectory()))
                            .createFileProvider())
                    .setChannelCountProvider(
                        StorageChannelCountProvider.New(
                            Math.max(
                                1, // minimum one channel, if only 1 core is available
                                Integer.highestOneBit(
                                    Runtime.getRuntime().availableProcessors() - 1))))
                    .createConfiguration());

    // handle changing class definitions at runtime
    foundation.onConnectionFoundation(
        connectionFoundation ->
            connectionFoundation.setClassLoaderProvider(
                ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())));
    return foundation.createEmbeddedStorageManager().start();
  }
}
