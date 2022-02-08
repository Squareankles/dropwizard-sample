package com.example.core.model;

import com.example.db.entity.DataSyncStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusValidationHolder {

  private final DataSyncStatus current;

  private final DataSyncStatus expected;
}
