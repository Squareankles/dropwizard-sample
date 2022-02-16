package com.example.api;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerResponse {

  private final List<Customer> customers;

  @Getter
  @AllArgsConstructor
  public static class Customer {

    private final NameFamily name;

    private final AddressFamily address;

    private final ContactFamily contact;


    @Getter
    @AllArgsConstructor
    public static class NameFamily {

      private final String firstName;

      private final String lastName;
    }

    @Getter
    @AllArgsConstructor
    public static class AddressFamily {

      private final String line1;

      private final String line2;

      private final String city;
    }

    @Getter
    @AllArgsConstructor
    public static class ContactFamily {

      private final String landLine;

      private final String mobile;
    }
  }

}
