package com.adme.learning.loan.model.mapper.common;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

  private UUID id = UUID.randomUUID();
  private String eventName;
  private String source;
  private String payload;

}
