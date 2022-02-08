package com.example.db.entity;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DataSyncOperation {

  public UUID operationId;

  public String customerId;

  public ZonedDateTime created;

}
