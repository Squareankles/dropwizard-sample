package com.example.core.service;

import com.google.inject.ImplementedBy;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@ImplementedBy(FileSyncServiceImpl.class)
public interface FileSyncService {

  void upload(UUID operationId, File stream, String fileName)
      throws ExecutionException, InterruptedException;

}
