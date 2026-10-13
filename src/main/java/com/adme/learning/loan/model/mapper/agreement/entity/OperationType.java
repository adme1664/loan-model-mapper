package com.adme.learning.loan.model.mapper.agreement.entity;

public enum OperationType {
  INSERT("INSERT"), MODIFY("MODIFY"), REMOVE("REMOVE");

  private final String value;

  OperationType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
