package com.example.core.service;

import static com.example.db.entity.DataSyncStatus.CANCELLED;
import static com.example.db.entity.DataSyncStatus.COMPLETED;
import static com.example.db.entity.DataSyncStatus.PARSING;
import static com.example.db.entity.DataSyncStatus.SUBMITTED;
import static java.time.ZoneOffset.UTC;

import com.example.client.FileSyncClient;
import com.example.core.mapper.Mapper;
import com.example.core.model.StatusValidationHolder;
import com.example.core.validator.Validator;
import com.example.db.dao.CustomerDao;
import com.example.db.dao.FileSyncDao;
import com.example.db.dao.SyncOperationDao;
import com.example.db.dao.SyncOperationStatusDao;
import com.example.db.entity.Customer;
import com.example.db.entity.DataSyncOperation;
import com.example.db.entity.FileSync;
import com.example.db.entity.OperationStatus;
import com.google.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileProcessingServiceImpl implements FileProcessingService {

  private final FileSyncDao fileSyncDao;

  private final SyncOperationStatusDao operationStatusDao;

  private final FileSyncClient fileSyncClient;

  private final CustomerDao customerDao;

  private final Validator<StatusValidationHolder> statusValidator;

  private final Mapper<String, List<Customer>> customerMapper;

  private final SyncOperationDao syncOperationDao;

  @Inject
  public FileProcessingServiceImpl(FileSyncDao fileSyncDao,
      SyncOperationStatusDao operationStatusDao, FileSyncClient fileSyncClient,
      CustomerDao customerDao,
      Validator<StatusValidationHolder> statusValidator,
      Mapper<String, List<Customer>> customerMapper,
      com.example.db.dao.SyncOperationDao syncOperationDao) {
    this.fileSyncDao = fileSyncDao;
    this.operationStatusDao = operationStatusDao;
    this.fileSyncClient = fileSyncClient;
    this.customerDao = customerDao;
    this.statusValidator = statusValidator;
    this.customerMapper = customerMapper;
    this.syncOperationDao = syncOperationDao;
  }

  @Override
  public void process(String id) {
    try {

      // Get the file key
      FileSync fileSync = this.fileSyncDao.get(id);

      // Check the status of the operation
      OperationStatus currentOperationStatus = this.operationStatusDao.get(id);

      this.statusValidator
          .validate(new StatusValidationHolder(currentOperationStatus.getStatus(), SUBMITTED));

      this.operationStatusDao
          .create(new OperationStatus(UUID.randomUUID(), UUID.fromString(id), PARSING, ZonedDateTime
              .now(UTC)));

      // Download the file
      String fileContent = this.fileSyncClient.download(fileSync.getFileKey());

      // Parse the file
      List<Customer> customers = this.customerMapper.map(fileContent);

      // Save the details to hbase
      DataSyncOperation dataSyncOperation = this.syncOperationDao.get(id);

      this.customerDao.create(customers, dataSyncOperation.customerId);
      // Update the status in reality would need better exception handling for adding to a queue when something fails
      this.operationStatusDao
          .create(
              new OperationStatus(UUID.randomUUID(), UUID.fromString(id), COMPLETED, ZonedDateTime
                  .now(UTC)));

    } catch (Exception e) {
      log.error(e.getMessage());
      this.operationStatusDao.create(
          new OperationStatus(UUID.randomUUID(), UUID.fromString(id), CANCELLED,
              ZonedDateTime.now(UTC)));
    }
  }

}
