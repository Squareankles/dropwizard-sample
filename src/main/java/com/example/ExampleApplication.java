package com.example;

import com.example.cli.HBaseCommand;
import com.example.core.exceptionmapper.HTTPStatusExceptionMapper;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.guicey.jdbi3.JdbiBundle;

public class ExampleApplication extends Application<ExampleConfiguration> {


  private static final String TOPIC = "DATA_SYNC.STRING.FILE_SYNC_REQUEST";

  public static void main(String[] args) throws Exception {
    new ExampleApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {

    bootstrap.addCommand(new HBaseCommand());

    bootstrap.addBundle(new MigrationsBundle<>() {
      @Override
      public DataSourceFactory getDataSourceFactory(ExampleConfiguration configuration) {
        return configuration.getDatabase();
      }

      @Override
      public String getMigrationsFileName() {
        return "migrations.xml";
      }
    });
    bootstrap.addBundle(new MultiPartBundle());


    bootstrap.addBundle(GuiceBundle.builder()
        .bundles(JdbiBundle.<ExampleConfiguration>forDatabase((conf, env) -> conf.getDatabase()))
        .enableAutoConfig("com.example.db.dao", "com.example.core.service",
            "com.example.resources", "com.example.health")
        .modulesOverride(new ExampleModule())

        .build());

  }

  @Override
  public void run(ExampleConfiguration exampleConfiguration, Environment environment)
      throws Exception {
    environment.jersey().register(HTTPStatusExceptionMapper.class);

  }
}
