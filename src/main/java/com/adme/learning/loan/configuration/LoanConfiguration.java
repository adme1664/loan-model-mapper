package com.adme.learning.loan.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.JacksonJsonEncoder;
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

  @Bean(name = "agreementWebClient")
  public WebClient agreementWebClient() {
    ObjectMapper objectMapper = new ObjectMapper();

    // Register custom module to convert LocalDate to Instant format
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDate.class, new LocalDateToInstantSerializer());
    objectMapper.registerModule(module);

    // Register JSR-310 module for other date/time types
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.findAndRegisterModules();

    return WebClient.builder()
        .baseUrl(agreementUrl)
        .codecs(configurer -> {
          configurer.defaultCodecs().jacksonJsonEncoder(new JacksonJsonEncoder());
          configurer.defaultCodecs().jacksonJsonEncoder(new JacksonJsonEncoder());
        })
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
