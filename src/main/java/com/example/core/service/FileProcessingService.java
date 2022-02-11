package com.example.core.service;

import com.google.inject.ImplementedBy;

@ImplementedBy(FileProcessingServiceImpl.class)
public interface FileProcessingService {

  void process(String id);

}
