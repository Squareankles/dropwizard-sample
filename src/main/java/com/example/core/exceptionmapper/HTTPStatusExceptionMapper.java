package com.example.core.exceptionmapper;

import com.example.core.exception.HTTPStatusException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class HTTPStatusExceptionMapper implements ExceptionMapper<HTTPStatusException> {

  @Override
  public Response toResponse(HTTPStatusException e) {
    ResponseMessage responseMessage = new ResponseMessage(e.getDescription(), e.getStatus().toString());
    log.info("corrolation message = " + responseMessage.getRefId().toString());
    return Response.status(e.getStatus())
        .entity(responseMessage)
        .type(MediaType.APPLICATION_JSON).build();
  }

  @AllArgsConstructor
  @Getter
  private class ResponseMessage {

    private final String message;

    private final String status;

    private final String time = ZonedDateTime.now(ZoneOffset.UTC).toString();

    private final UUID refId = UUID.randomUUID();

  }
}
