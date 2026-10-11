package com.adme.learning.loan.model.mapper.agreement.control;

import com.adme.learning.loan.model.mapper.agreement.entity.BaseSource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ParsingMessage {

  private final ObjectMapper objectMapper;

  public ParsingMessage() {
    this.objectMapper = new ObjectMapper();

//    // Configure for lenient parsing
//    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//    objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
//    objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
//    objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
//    objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
//    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//
//    // Register JSR-310 module for modern date/time types
//    objectMapper.registerModule(new JavaTimeModule());
//    objectMapper.findAndRegisterModules();

  }

  public <T>  List<T> parseMessage(String message, Class<? extends BaseSource> clazz)
      throws JsonProcessingException {
    if (message == null || message.isEmpty()) {
      throw new IllegalArgumentException("Message cannot be null or empty");
    }
    try {
      return (List<T>) parseMessage(objectMapper.readValue(message,
          objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)));
    } catch (JsonProcessingException e) {
      log.error("JSON parsing error. Message content: {} " , message);
      log.error("Error details: {}" , e.getMessage(), e);
      e.printStackTrace();
      throw new JsonProcessingException("Failed to parse JSON into " + clazz.getSimpleName() + ": " + e.getOriginalMessage()) {};
    } catch (Exception e) {
      log.error("Unexpected error during JSON parsing. Message: {}" , message, e);
      e.printStackTrace();
      throw new RuntimeException("Unexpected error parsing message into " + clazz.getSimpleName(), e);
    }
  }

  private List<? extends BaseSource> parseMessage(List<? extends BaseSource> sources) {
    // Implementation for parsing a single message
    if (sources == null || sources.isEmpty()) {
      return new ArrayList<>();
    }

    List<BaseSource> regularSources = new ArrayList<>();
    List<BaseSource> endSources = new ArrayList<>();

    sources.forEach(source -> {
      if (source.getId().startsWith("END")) {
        endSources.add(source);
      } else {
        regularSources.add(source);
      }
    });

    regularSources.addAll(endSources);

    return regularSources;
  }

}
