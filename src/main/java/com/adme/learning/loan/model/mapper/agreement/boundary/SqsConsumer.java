package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.loan.model.mapper.agreement.control.ParsingMessage;
import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSource;
import io.awspring.cloud.sqs.annotation.SqsListener;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SqsConsumer {

  private final ParsingMessage parsingMessage;

  public SqsConsumer(ParsingMessage parsingMessage) {
    this.parsingMessage = parsingMessage;
  }

  @SqsListener(value = "${sqs.queue.agreement.name}")
  public void consumeAgreement(String message) {
    try {
      log.info("Received message: {}", message);

      List<AgreementSource> agreementSources = parsingMessage.parseMessage(message,
          AgreementSource.class);

    } catch (Exception e) {
      log.error("Error occurred while parsing message: {}", message, e);
    }

  }

}
