package com.adme.learning.loan.model.mapper.agreement.boundary;

public enum EndEvent {
  END_BALANCE("end.balance.process"),
  END_SCHEDULE("end.schedule.process"),
  END_AGREEMENT("end.agreement.process");

  private final String value;

  EndEvent(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
