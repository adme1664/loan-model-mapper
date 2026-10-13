package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.loan.model.mapper.agreement.entity.BaseSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MessageParsing {

  private final ObjectMapper objectMapper;

  public MessageParsing() {
    this.objectMapper = new ObjectMapper();
  }

  public <T> List<T> parseMessage(String message, Class<? extends BaseSource> clazz) {
    if (message == null || message.isEmpty()) {
      throw new IllegalArgumentException("Message cannot be null or empty");
    }
    try {
      return objectMapper.readValue(message,
          objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to parse message", e);
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

    });

    return regularSources;
  }

}
