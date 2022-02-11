package com.example.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.inject.Inject;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.SneakyThrows;

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
    return fileKey;
  }

  @SneakyThrows
  public String download(String fileKey) {
    S3Object s3object = s3.getObject(new GetObjectRequest(BUCKET_NAME, fileKey));
    S3ObjectInputStream inputStream = s3object.getObjectContent();
    return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
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
