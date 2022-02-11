package com.example.client;

import com.google.inject.ImplementedBy;
import java.io.File;
import java.util.UUID;

@ImplementedBy(FileSyncClientImpl.class)
public interface FileSyncClient {

  String upload(File file, UUID folderName, String fileName);

  String download(String fileKey);

  String getUrl(String fileKey);

  void remove(String fileKey);

}
