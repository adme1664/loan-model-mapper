package com.adme.learning.loan.model.mapper.common;

public class LogMessage {

  private LogMessage() {

  }

  public static final String ERROR_MESSAGE_PROCESSING = "Error occurred while processing {} source: {}";
  public static final String ERROR_MESSAGE_PARSING = "Error occurred while parsing {} source: {}";
  public static final String SUCCESS_MESSAGE_PROCESSING = "Successfully processed {} with ID={} source: {}";

}
