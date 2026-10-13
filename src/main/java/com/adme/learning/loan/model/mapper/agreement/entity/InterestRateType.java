package com.adme.learning.loan.model.mapper.agreement.entity;

public enum InterestRateType {
  FIXED("FIXED"),
  VARIABLE("VARIABLE");

  private final String value;

  InterestRateType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
