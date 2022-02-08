package com.example.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class SyncOperationRequest {

  @Length(min = 5, max = 35)
  @JsonProperty
  @NotEmpty
  private String customerId;

  @JsonProperty
  @NotNull
  private UUID operationId;

}
