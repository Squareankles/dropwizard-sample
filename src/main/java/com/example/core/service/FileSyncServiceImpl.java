package com.example.core.service;

import static com.example.db.entity.DataSyncStatus.OPENED;
import static com.example.db.entity.DataSyncStatus.SUBMITTED;
import static java.time.ZoneOffset.UTC;

import com.codahale.metrics.annotation.Timed;
import com.example.client.FileSyncClient;
import com.example.core.model.StatusValidationHolder;
import com.example.core.validator.Validator;
import com.example.db.dao.FileSyncDao;
import com.example.db.dao.SyncOperationStatusDao;
import com.example.db.entity.FileSync;
import com.example.db.entity.OperationStatus;
import com.example.producer.FileSyncProducer;
import com.google.inject.Inject;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.vyarus.guicey.jdbi3.tx.TransactionTemplate;

@Slf4j
public class FileSyncServiceImpl implements FileSyncService {

  private final FileSyncClient fileSyncClient;

  private final SyncOperationStatusDao syncOperationStatusDao;

  private final Validator<StatusValidationHolder> statusValidator;

  private final FileSyncProducer fileSyncProducer;

  private final FileSyncDao fileSyncDao;

  private final TransactionTemplate transactionTemplate;

  @Inject
  public FileSyncServiceImpl(FileSyncClient fileSyncClient,
      SyncOperationStatusDao syncOperationStatusDao,
      Validator<StatusValidationHolder> statusValidator,
      FileSyncProducer fileSyncProducer, FileSyncDao fileSyncDao,
      TransactionTemplate transactionTemplate) {
    this.fileSyncClient = fileSyncClient;
    this.syncOperationStatusDao = syncOperationStatusDao;
    this.statusValidator = statusValidator;
    this.fileSyncProducer = fileSyncProducer;
    this.fileSyncDao = fileSyncDao;
    this.transactionTemplate = transactionTemplate;
  }

  @Timed
  @SneakyThrows
  @Override
  public void upload(UUID operationId, File file, String fileName) {
    log.info("Got here 1");
    String fileKey = null;
    final String key;
    final UUID uploadKey = UUID.randomUUID();
    final ZonedDateTime now = ZonedDateTime.now(UTC);

    try {

      // Check the status of the operation

      CompletableFuture<OperationStatus> opStatusFuture = CompletableFuture
          .supplyAsync(() -> this.syncOperationStatusDao.get(operationId.toString()));
      CompletableFuture<String> fileSyncFuture = CompletableFuture
          .supplyAsync(() -> this.fileSyncClient.upload(file, uploadKey, fileName));

      CompletableFuture.allOf(fileSyncFuture, opStatusFuture).get();
      OperationStatus operationStatus = opStatusFuture.get();
      fileKey = fileSyncFuture.get();
      key = fileKey;
      String fileLocation = this.fileSyncClient.getUrl(fileKey);

      this.statusValidator
          .validate(new StatusValidationHolder(operationStatus.getStatus(), OPENED));

      transactionTemplate.inTransaction((handle) -> {

        // Save the upload record
        this.fileSyncDao.create(new FileSync(uploadKey, operationId, fileLocation, key, now));
        // Update the status
        this.syncOperationStatusDao
            .create(new OperationStatus(UUID.randomUUID(), operationId, SUBMITTED, now));
        return null;
      });

      // Send to kafka
      this.fileSyncProducer.send(operationId);

    } catch (Exception e) {
      if (fileKey != null) {
        this.fileSyncClient.remove(fileKey);
      }

      throw e;
    }


  }
}
