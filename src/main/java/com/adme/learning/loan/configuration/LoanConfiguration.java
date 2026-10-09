package com.adme.learning.loan.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class LoanConfiguration {

  private final static String REGION = "ca-central-1";

  @Bean
  public S3Client s3Instance() {
    return S3Client.builder().region(Region.of(REGION)).build();
  }


  @Bean
  public SqsClient sqsInstance() {
    return SqsClient.builder().region(Region.of(REGION)).build();
  }

}
