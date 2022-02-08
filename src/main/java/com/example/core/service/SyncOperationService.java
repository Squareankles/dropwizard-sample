package com.example.core.service;

import com.example.api.SyncOperationRequest;
import com.google.inject.ImplementedBy;
import java.util.UUID;

@ImplementedBy(SyncOperationServiceImpl.class)
public interface SyncOperationService {

  UUID createOperation(SyncOperationRequest request);

}
