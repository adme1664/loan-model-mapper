package com.adme.learning.loan.model.mapper.agreement.boundary;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class SqsConsumer {

  @SqsListener(value = "${sqs.queue.agreement.name}")
  public void consumeAgreement(String message) {

  }

}
