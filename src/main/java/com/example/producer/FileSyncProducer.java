package com.example.producer;

import com.google.inject.ImplementedBy;
import java.util.UUID;

@ImplementedBy(FileSyncProducerImpl.class)
public interface FileSyncProducer {

  void send(UUID id);

}
