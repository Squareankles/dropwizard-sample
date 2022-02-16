package com.example;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.api.CustomerResponse;
import com.example.consumer.FileSyncConsumer;
import com.example.core.mapper.CustomerContactsMapper;
import com.example.core.mapper.CustomerListMapper;
import com.example.core.mapper.Mapper;
import com.example.core.model.StatusValidationHolder;
import com.example.core.validator.StatusValidator;
import com.example.core.validator.Validator;
import com.example.db.entity.Customer;
import com.google.inject.TypeLiteral;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import lombok.SneakyThrows;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

public class ExampleModule extends DropwizardAwareModule<ExampleConfiguration> {

  @SneakyThrows
  @Override
  protected void configure() {

    // example access to dropwizard objects from module
    configuration();
    environment();
    bootstrap();

    bind(new TypeLiteral<Validator<StatusValidationHolder>>() {
    }).to(StatusValidator.class);
    bind(new TypeLiteral<Mapper<String, List<Customer>>>() {
    }).to(CustomerContactsMapper.class);
    bind(new TypeLiteral<Mapper<List<Customer>, CustomerResponse>>() {
    }).to(CustomerListMapper.class);
    bindAWSS3Client();
    bindKafkaProducer();
    bindKafkaConsumer();
    bindHBaseConnection();
  }

  private void bindAWSS3Client() {
    final AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
        "http://localhost:4566", "");

    bind(AmazonS3.class)
        .toInstance(AmazonS3ClientBuilder.standard().withPathStyleAccessEnabled(true)
            .withEndpointConfiguration(endpoint)
            .withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials("test", "test")))
            .build());
  }

  private void bindKafkaProducer() {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:29092");
    props.put("acks", "all");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    bind(new TypeLiteral<Producer<String, String>>() {
    }).toInstance(new KafkaProducer<>(props));
  }

  private void bindKafkaConsumer() {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:29092");
    props.setProperty("enable.auto.commit", "false");
    props.setProperty("group.id", "8");
    props.setProperty("key.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer consumer = new KafkaConsumer<>(props);
    bind(new TypeLiteral<Consumer<String, String>>() {
    }).toInstance(consumer);
    bind(FileSyncConsumer.class).asEagerSingleton();
  }

  private void bindHBaseConnection() throws IOException {
    Configuration hBaseConfig = HBaseConfiguration.create();

    Connection connection = ConnectionFactory.createConnection(hBaseConfig);
    bind(Connection.class).toInstance(connection);
  }

}
