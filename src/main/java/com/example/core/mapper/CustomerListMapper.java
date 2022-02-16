package com.example.core.mapper;

import com.example.api.CustomerResponse;
import com.example.api.CustomerResponse.Customer.AddressFamily;
import com.example.api.CustomerResponse.Customer.ContactFamily;
import com.example.api.CustomerResponse.Customer.NameFamily;
import com.example.db.entity.Customer;
import java.util.ArrayList;
import java.util.List;

public class CustomerListMapper implements Mapper<List<Customer>, CustomerResponse> {

  @Override
  public CustomerResponse map(List<Customer> from) {

    CustomerResponse customerResponse = new CustomerResponse(new ArrayList<>());

    for (Customer c : from) {
      customerResponse.getCustomers().add(new CustomerResponse.Customer(
          new NameFamily(c.getNameFamily().getFirstName(), c.getNameFamily().getLastName()),
          new AddressFamily(c.getAddressFamily().getLine1(), c.getAddressFamily().getLine2(),
              c.getAddressFamily().getCity()),
          new ContactFamily(c.getContactFamily().getLandLine(), c.getContactFamily().getMobile())));
    }
    return customerResponse;
  }
}
