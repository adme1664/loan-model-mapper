package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.loan.model.mapper.agreement.control.AgreementBundleContext;
import com.adme.learning.loan.model.mapper.agreement.control.ParsingMessage;
import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSource;
import com.adme.learning.model.generator.AgreementBundleDTO;
import io.awspring.cloud.sqs.annotation.SqsListener;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SqsConsumer {

  private final ParsingMessage parsingMessage;
  private final ProcessingAgreement processingAgreement;

  public SqsConsumer(ParsingMessage parsingMessage, ProcessingAgreement processingAgreement) {
    this.parsingMessage = parsingMessage;
    this.processingAgreement = processingAgreement;
  }

  @SqsListener(value = "${sqs.queue.agreement.name}")
  public void consumeAgreement(String message) {
    try {

      List<AgreementSource> agreementSources = parsingMessage.parseMessage(message,
          AgreementSource.class);

      if (agreementSources.isEmpty()) {
        log.warn("No agreement sources found in the message: {}", message);

      } else {
        agreementSources.forEach(source -> {
          try {
            AgreementBundleContext context = new AgreementBundleContext(source.getId(),
                source.getEventName(), source.getBusinessDt());

            AgreementBundleDTO agreementBundleDTO = processingAgreement.prepareAgreementBundle(
                source, context);

            log.info("Processing agreement with AgreementNo={}, AgreementType={}, Operation={}",
                source.getId(), source.getAgmtTypeCd(), source.getEventName());

            processingAgreement.synchronize(agreementBundleDTO);

            log.info("Agreement with AgreementNo={}, AgreementType={}, Operation={} with successfully processed.",
                source.getId(), source.getAgmtTypeCd(), source.getEventName());

          } catch (Exception e) {
            log.error("Error occurred while processing agreement source: {}", e.getMessage());
          }
        });
      }
    } catch (Exception e) {
      log.error("Error occurred while parsing message: {}", e.getMessage());
    }

  }

}
