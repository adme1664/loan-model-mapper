package com.adme.learning.loan.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Custom Jackson serializer that converts LocalDate to Instant format (ISO-8601 with timezone).
 * Converts "2024-04-05" to "2024-04-05T00:00:00Z"
 */
public class LocalDateToInstantSerializer extends JsonSerializer<LocalDate> {

  @Override
  public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (value != null) {
      String instantString = value.atStartOfDay(ZoneOffset.UTC)
          .format(DateTimeFormatter.ISO_INSTANT);
      gen.writeString(instantString);
    }
  }
}