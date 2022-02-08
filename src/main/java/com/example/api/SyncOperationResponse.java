package com.example.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SyncOperationResponse {

  private final String syncId;

  public SyncOperationResponse(String syncId) {
    this.syncId = syncId;
  }

  @JsonProperty
  public String getSyncId() {
    return syncId;
  }
}
