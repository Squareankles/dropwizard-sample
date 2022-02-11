package com.example.db.entity;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileSync {

  private final UUID id;

  private final UUID operationId;

  private final String location;

  private final String fileKey;

  private final ZonedDateTime created;

}
