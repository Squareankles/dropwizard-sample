package com.example.health;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import ru.vyarus.dropwizard.guice.module.installer.feature.health.NamedHealthCheck;

@Slf4j
public class S3HealthCheck extends NamedHealthCheck {

  @Inject
  private AmazonS3 s3;

  @Override
  protected Result check() throws Exception {
    Result result;
    try {
      s3.getS3AccountOwner();
      result = Result.healthy();
    } catch (Exception e) {
      result = Result.unhealthy(e);
    }

    return result;
  }

  @Override
  public String getName() {
    return "S3";
  }
}
