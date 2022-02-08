package com.example.core.service;

import static com.example.db.entity.DataSyncStatus.OPENED;
import static java.time.ZoneOffset.UTC;

import com.example.api.SyncOperationRequest;
import com.example.db.dao.SyncOperationDao;
import com.example.db.dao.SyncOperationStatusDao;
import com.example.db.entity.DataSyncOperation;
import com.example.db.entity.OperationStatus;
import com.google.inject.Inject;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

@Slf4j
public class SyncOperationServiceImpl implements SyncOperationService {

  @Inject
  private SyncOperationDao syncOperationDao;

  @Inject
  private SyncOperationStatusDao syncOperationStatusDao;


  @InTransaction
  @Override
  public UUID createOperation(SyncOperationRequest request) {
    final ZonedDateTime created = ZonedDateTime.now(UTC);

    this.syncOperationDao
        .create(new DataSyncOperation(request.getOperationId(), request.getCustomerId(), created));
    this.syncOperationStatusDao
        .create(new OperationStatus(UUID.randomUUID(), request.getOperationId(), OPENED,
            created));

    return request.getOperationId();
  }
}
