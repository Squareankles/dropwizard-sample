package com.example.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

import com.example.core.service.FileSyncService;
import com.google.inject.Inject;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Slf4j
@Path("/v1/filesync")
public class FileSyncResource {

  private final FileSyncService fileSyncService;

  @Inject
  FileSyncResource(FileSyncService fileSyncService) {
    this.fileSyncService = fileSyncService;
  }

  @POST
  @Path("/{operationId}")
  @Consumes(MULTIPART_FORM_DATA)
  @Produces(APPLICATION_JSON)
  public Response create(
      @PathParam("operationId") UUID operationId,
      @FormDataParam("file") File uploadedFile,
      @FormDataParam("file") FormDataContentDisposition fileMetaData)
      throws ExecutionException, InterruptedException {
    this.fileSyncService.upload(operationId, uploadedFile, fileMetaData.getFileName());
    return Response.status(Status.CREATED).build();
  }

}
