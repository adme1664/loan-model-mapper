package com.adme.learning.loan.model.mapper.agreement.entity;

public enum LoanBalanceType {
  ACCRUAL_INTEREST("ACCRUAL_INTEREST"),
  GLOBAL_ORIGINAL("GLOBAL_ORIGINAL"),
  CURRENT_ORIGINAL("CURRENT_ORIGINAL"),
  BANK_GROSS("BANK_GROSS"),
  BANK_NET("BANK_NET"),
  PARTICIPANT("PARTICIPANT");

  private final String value;

  LoanBalanceType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
