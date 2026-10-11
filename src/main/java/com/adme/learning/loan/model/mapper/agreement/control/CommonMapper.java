package com.adme.learning.loan.model.mapper.agreement.control;

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
    return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        .atStartOfDay(ZoneId.of("UTC")).toInstant().toString();
  }

}
