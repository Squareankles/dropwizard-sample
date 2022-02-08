package com.example.core.exception;

import javax.ws.rs.core.Response.Status;
import lombok.Getter;

@Getter
public class QueueDeliveryException extends HTTPStatusException {

  private final Status status = Status.INTERNAL_SERVER_ERROR;

  private final String description = "Unfortunately we could not complete this request please try again later";
}
