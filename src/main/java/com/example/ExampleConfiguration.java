package com.example;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ExampleConfiguration extends Configuration {

  @Valid
  @NotNull
  private final DataSourceFactory database = new DataSourceFactory();

  @NotEmpty
  private String name;

}
