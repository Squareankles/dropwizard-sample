package com.example.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.example.api.SyncOperationResponse;
import com.example.api.SyncOperationRequest;
import com.example.core.service.SyncOperationService;
import com.google.inject.Inject;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/v1/syncoperation")
@Produces(APPLICATION_JSON)
public class SyncOperationResource {

  @Inject
  private SyncOperationService syncOperationService;

  @POST
  public SyncOperationResponse create(@NotNull @Valid SyncOperationRequest request) {

    UUID operationId = this.syncOperationService.createOperation(request);
    return new SyncOperationResponse(operationId.toString());
  }

}
