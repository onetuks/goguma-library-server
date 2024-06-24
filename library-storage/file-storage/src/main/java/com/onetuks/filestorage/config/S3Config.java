package com.onetuks.filestorage.config;

import java.net.URI;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  @Getter
  @Value("${aws.endpoint}")
  private String endpoint;

  @Getter
  @Value("${aws.bucket-name}")
  private String bucketName;

  @Value("${aws.access-key-id}")
  private String accessKeyId;

  @Value("${aws.secret-access-key}")
  private String secretAccessKey;

  @Bean
  public AwsCredentialsProvider awsCredentialsProvider() {
    return AwsCredentialsProviderChain.builder()
        .reuseLastProviderEnabled(true)
        .credentialsProviders(
            List.of(
                DefaultCredentialsProvider.create(),
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKeyId, secretAccessKey))))
        .build();
  }

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .credentialsProvider(awsCredentialsProvider())
        .region(Region.AP_NORTHEAST_2)
        .endpointOverride(URI.create(endpoint))
        .build();
  }
}
