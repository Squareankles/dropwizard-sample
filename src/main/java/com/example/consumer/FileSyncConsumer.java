package com.example.consumer;

import com.example.core.service.FileProcessingService;
import com.google.inject.Inject;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import javax.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import ru.vyarus.guicey.annotations.lifecycle.PostStartup;

@Slf4j
public class FileSyncConsumer extends Thread {

  private final Consumer<String, String> consumer;

  private final FileProcessingService fileProcessingService;

  @Inject
  public FileSyncConsumer(Consumer<String, String> consumer,
      FileProcessingService fileProcessingService) {
    this.consumer = consumer;
    this.fileProcessingService = fileProcessingService;
  }

  @PostStartup
  private void afterStartup() {
    consumer.subscribe(Collections.singleton("DATA_SYNC.STRING.FILE_SYNC_REQUEST"));
    this.start();
  }

  @PreDestroy
  private void stopBean() {
    consumer.close(Duration.ofMillis(7000));
  }

  @SneakyThrows
  public void run() {
    try {
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(50000));
        for (TopicPartition partition : records.partitions()) {

          List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);

          for (ConsumerRecord<String, String> record : partitionRecords) {
            this.fileProcessingService.process(record.value());
            consumer.commitAsync(
                Collections.singletonMap(partition, new OffsetAndMetadata(record.offset() + 1)),
                null);
          }

        }
      }
    } finally {
      consumer.close();
    }
  }
}
