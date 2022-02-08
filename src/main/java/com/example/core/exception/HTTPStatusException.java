package com.example.core.exception;

import javax.ws.rs.core.Response.Status;

public abstract class HTTPStatusException extends RuntimeException {

  public abstract Status getStatus();

  public abstract String getDescription();

}
