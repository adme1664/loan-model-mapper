package com.adme.learning.loan.model.mapper.agreement.control;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommonMapper {

  private CommonMapper() {
  }

  public static String convertInstantToString(String date) {
    if (date == null) {
      return null;
    }

    // Try to parse as ISO 8601 instant first (e.g., "2026-10-13T10:18:00.381272Z")
    try {
      return Instant.parse(date).toString();
    } catch (Exception e) {
      // Fall back to parsing as date only (e.g., "2026-10-13")
      return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
          .atStartOfDay(ZoneId.of("UTC")).toInstant().toString();
    }
  }

}
