package de.eemkeen.model;

import lombok.extern.slf4j.Slf4j;
import one.microstream.integrations.spring.boot.types.config.StorageManagerInitializer;
import one.microstream.storage.types.StorageManager;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Initializer implements StorageManagerInitializer {

  @Override
  public void initialize(StorageManager storageManager) {

    // Since we have @Storage used, we are sure that Root object is initialized in StorageManager
    // We only need to check if there is an initialization of data required or not (since we already
    // ran it before)

    Root root = (Root) storageManager.root();
    // Init 'database' with some data
    if (root.getUsers().isEmpty()) {
      // We have made changes outside the normal way (the repositories) that have automatic
      // storage through @Storage.
      // We store the root using an Eager Storer since we need to store all info anyway.
      storageManager.createEagerStorer().store(root);
    }
  }
}
