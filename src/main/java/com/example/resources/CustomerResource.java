package com.example.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.example.api.CustomerResponse;
import com.example.core.service.CustomerService;
import com.google.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/v1/customers")
@Produces(APPLICATION_JSON)
public class CustomerResource {

  private final CustomerService customerService;

  @Inject
  public CustomerResource(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GET
  public CustomerResponse get(@HeaderParam("user-id") @Valid @Size(min = 36, max = 36) String userId) {
    return this.customerService.get(userId);
  }

}
