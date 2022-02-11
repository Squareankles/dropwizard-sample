package com.example.core.service;

import static com.example.db.entity.DataSyncStatus.OPENED;
import static com.example.db.entity.DataSyncStatus.SUBMITTED;
import static java.time.ZoneOffset.UTC;

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
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

public class FileSyncServiceImpl implements FileSyncService {

  private final FileSyncClient fileSyncClient;

  private final SyncOperationStatusDao syncOperationStatusDao;

  private final Validator<StatusValidationHolder> statusValidator;

  private final FileSyncProducer fileSyncProducer;

  private final FileSyncDao fileSyncDao;

  @Inject
  public FileSyncServiceImpl(FileSyncClient fileSyncClient,
      SyncOperationStatusDao syncOperationStatusDao,
      Validator<StatusValidationHolder> statusValidator,
      FileSyncProducer fileSyncProducer, FileSyncDao fileSyncDao) {
    this.fileSyncClient = fileSyncClient;
    this.syncOperationStatusDao = syncOperationStatusDao;
    this.statusValidator = statusValidator;
    this.fileSyncProducer = fileSyncProducer;
    this.fileSyncDao = fileSyncDao;
  }

  @InTransaction
  @Override
  public void upload(UUID operationId, File file, String fileName) {

    String fileKey = null;
    final UUID uploadKey = UUID.randomUUID();
    final ZonedDateTime now = ZonedDateTime.now(UTC);

    try {
      // Check the status of the operation
      OperationStatus operationStatus = this.syncOperationStatusDao.get(operationId.toString());

      this.statusValidator
          .validate(new StatusValidationHolder(operationStatus.getStatus(), OPENED));

      // Upload the file
      fileKey = this.fileSyncClient.upload(file, uploadKey, fileName);
      String fileLocation = this.fileSyncClient.getUrl(fileKey);
      // Save the upload record
      this.fileSyncDao.create(new FileSync(uploadKey, operationId, fileLocation, fileKey, now));

      // Send to kafka
      this.fileSyncProducer.send(operationId);

      // Update the status
      this.syncOperationStatusDao
          .create(new OperationStatus(UUID.randomUUID(), operationId, SUBMITTED, now));

    } catch (Exception e) {
      if (fileKey != null) {
        this.fileSyncClient.remove(fileKey);
      }

      throw e;
    }


  }
}
