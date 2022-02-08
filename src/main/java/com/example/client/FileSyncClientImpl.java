package com.example.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.UUID;

public class FileSyncClientImpl implements FileSyncClient {

  private static final String BUCKET_NAME = "file-sync";

  private final AmazonS3 s3;

  @Inject
  public FileSyncClientImpl(AmazonS3 s3) {
    this.s3 = s3;
  }

  @Override
  public String upload(File file, UUID folderName, String fileName) {
    String fileKey = folderName.toString() + "/" + fileName;
    PutObjectResult result = s3
        .putObject(BUCKET_NAME, fileKey, file);
    URL url = s3.getUrl(BUCKET_NAME, fileKey);
    return fileKey;
  }

  @Override
  public String getUrl(String fileKey) {
    return s3.getUrl(BUCKET_NAME, fileKey).toString();
  }

  @Override
  public void remove(String fileKey) {

    s3.deleteObject(BUCKET_NAME, fileKey);
  }


}
