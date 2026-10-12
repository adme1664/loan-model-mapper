package com.adme.learning.loan.model.mapper.creditfacility.boundary;

import static com.adme.learning.loan.model.mapper.common.LogMessage.ERROR_MESSAGE_PARSING;
import static com.adme.learning.loan.model.mapper.common.LogMessage.SUCCESS_MESSAGE_PROCESSING;

import com.adme.learning.loan.model.mapper.common.BundleContext;
import com.adme.learning.loan.model.mapper.common.ParsingMessage;
import com.adme.learning.loan.model.mapper.creditfacility.entity.DealSource;
import io.awspring.cloud.sqs.annotation.SqsListener;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SqsDealConsumer {

  private final ParsingMessage parsingMessage;
  private final ProcessingCreditFacility processingCreditFacility;

  public SqsDealConsumer(ParsingMessage parsingMessage,
      ProcessingCreditFacility processingCreditFacility) {
    this.parsingMessage = parsingMessage;
    this.processingCreditFacility = processingCreditFacility;
  }

  @SqsListener(value = "${sqs.queue.deal}")
  public void consumeDealMessage(String message) {
    log.info("Received message: {}", message);
    String className = DealSource.class.getSimpleName();
    try {
      List<DealSource> dealSources = parsingMessage.parseMessage(message, DealSource.class);
      if (dealSources.isEmpty()) {
        log.warn("No deal sources found in the message: {}", message);
      } else {
        dealSources.forEach(dealSource -> {
          if (dealSource.getId().startsWith("end")) {
            log.info("Deal source is end event: {}", dealSource.getId());
          } else {
            BundleContext bundleContext = new BundleContext(dealSource.getId(),
                dealSource.getEventName(), dealSource.getBusinessDate());

            processingCreditFacility.prepareCreditFacilityBundle(dealSource, bundleContext);

            log.info(SUCCESS_MESSAGE_PROCESSING, className, dealSource.getId(),
                dealSource.getEventName());
          }

        });
      }
    } catch (Exception e) {
      log.error(ERROR_MESSAGE_PARSING, className, e.getMessage());
    }
  }

}
