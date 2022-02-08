package com.example.core.exception;

import javax.ws.rs.core.Response.Status;
import lombok.Getter;

@Getter
public class ValidStatusException extends HTTPStatusException {

  private final Status status = Status.CONFLICT;

  private final String description = "The operation is in an invalid state";

}
