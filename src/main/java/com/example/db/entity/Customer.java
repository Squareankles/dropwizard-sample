package com.example.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Customer {

  @JsonProperty("name")
  private final NameFamily nameFamily;

  @JsonProperty("address")
  private final AddressFamily addressFamily;

  @JsonProperty("contact")
  private final ContactFamily contactFamily;


  @Getter
  @AllArgsConstructor
  public static class NameFamily {

    private String firstName;

    private String lastName;
  }

  @Getter
  @AllArgsConstructor
  public static class AddressFamily {

    private String line1;

    private String line2;

    private String city;
  }

  @Getter
  @AllArgsConstructor
  public static class ContactFamily {

    @JsonProperty("landLineNumber")
    private String landLine;

    private String mobile;
  }

}
