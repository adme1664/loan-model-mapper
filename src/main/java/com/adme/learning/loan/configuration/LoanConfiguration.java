package com.adme.learning.loan.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class LoanConfiguration {

  private final String awsRegion;
  private final String agreementUrl;

  public LoanConfiguration(@Value("${aws.region}") String awsRegion,
      @Value("${agreement.api.base-url}") String agreementUrl) {
    this.awsRegion = awsRegion;
    this.agreementUrl = agreementUrl;
  }

  @Bean(name = "balanceTopic")
  public NewTopic balanceTopic() {
    return TopicBuilder.name("3857.dev.nrt.balances.info")
        .partitions(1)
        .replicas(1)
        .build();
  }

  @Bean(name = "transactionTopic")
  public NewTopic transactionTopic() {
    return TopicBuilder.name("3857.dev.nrt.transactions.info")
        .partitions(1)
        .replicas(1)
        .build();
  }

  @Bean
  public Gson gson() {
    return new GsonBuilder().serializeNulls().create();
  }

  @Bean(name = "agreementWebClient")
  public WebClient agreementWebClient() {

    return WebClient.builder()
        .baseUrl(agreementUrl)

        .build();
  }

  @Bean
  public S3Client s3Instance() {
    return S3Client.builder().region(Region.of(awsRegion)).build();
  }


  @Bean
  public SqsClient sqsInstance() {
    return SqsClient.builder().region(Region.of(awsRegion)).build();
  }

}
