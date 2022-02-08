package com.example.producer;

import com.example.core.exception.QueueDeliveryException;
import com.google.inject.Inject;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
public class FileSyncProducerImpl implements FileSyncProducer {

  private static final String TOPIC = "DATA_SYNC.STRING.FILE_SYNC_REQUEST";

  private final Producer<String, String> producer;

  @Inject
  public FileSyncProducerImpl(Producer<String, String> producer) {
    this.producer = producer;
  }


  @Override
  public void send(UUID value) {
    try {
      RecordMetadata metadata = producer.send(new ProducerRecord<>(TOPIC, value.toString())).get();
      metadata.toString();
    } catch (InterruptedException | ExecutionException e) {
      log.error(e.getMessage());
      throw new QueueDeliveryException();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new QueueDeliveryException();
    }

  }
}
