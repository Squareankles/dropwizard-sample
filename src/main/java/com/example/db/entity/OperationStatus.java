package com.example.db.entity;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OperationStatus {

  private final UUID operationStatusId;

  private final UUID operationId;

  private final DataSyncStatus status;

  private final ZonedDateTime created;

}
