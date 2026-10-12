package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.loan.model.mapper.agreement.control.ParsingMessage;
import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSource;
import com.adme.learning.loan.model.mapper.agreement.entity.BalanceSource;
import com.adme.learning.loan.model.mapper.agreement.entity.BaseSource;
import com.adme.learning.loan.model.mapper.agreement.entity.ScheduleSource;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SqsConsumer {

  private final ParsingMessage parsingMessage;
  private final AgreementPersistence agreementPersistence;
  private final BalancePersistence balancePersistence;
  private final AgreementSchedulePersistence schedulePersistence;
  private final ProcessEndEvent processEndEvent;

  private static final String ERROR_MESSAGE_PROCESSING = "Error occurred while processing {} source: {}";
  private static final String ERROR_MESSAGE_PARSING = "Error occurred while parsing {} source: {}";
  private static final String SUCCESS_MESSAGE_PROCESSING = "Successfully processed {} with ID={} source: {}";

  public SqsConsumer(ParsingMessage parsingMessage,
      AgreementPersistence agreementPersistence, BalancePersistence balancePersistence,
      AgreementSchedulePersistence schedulePersistence, ProcessEndEvent processEndEvent) {
    this.parsingMessage = parsingMessage;
    this.agreementPersistence = agreementPersistence;
    this.balancePersistence = balancePersistence;
    this.schedulePersistence = schedulePersistence;
    this.processEndEvent = processEndEvent;
  }

  @SqsListener(value = "${sqs.queue.agreement}")
  public void consumeAgreement(String message) throws JsonProcessingException {

    String className = AgreementSource.class.getSimpleName();

    try {

      List<AgreementSource> agreementSources = parsingMessage.parseMessage(message,
          AgreementSource.class);

      if (agreementSources.isEmpty()) {
        log.warn("No agreement sources found in the message: {}", message);

      } else {
        agreementSources.forEach(source -> {
          try {
            agreementPersistence.saveOrUpdate(source);

            logInfo(SUCCESS_MESSAGE_PROCESSING, source.getClass().getSimpleName(), source.getId());

            processEndEventMessage(source);

          } catch (Exception e) {
            logError(ERROR_MESSAGE_PROCESSING, className, e.getMessage());
          }
        });
      }
    } catch (Exception e) {
      logError(ERROR_MESSAGE_PARSING, className, e.getMessage());
      throw e;
    }

  }

  @SqsListener(value = "${sqs.queue.schedule}")
  public void consumeSchedule(String message) throws JsonProcessingException {
    String className = ScheduleSource.class.getSimpleName();
    try {

      List<ScheduleSource> scheduleSources = parsingMessage.parseMessage(message,
          ScheduleSource.class);

      if (scheduleSources.isEmpty()) {
        log.warn("No schedule sources found in the message: {}", message);

      } else {
        scheduleSources.forEach(source -> {
          try {

            schedulePersistence.saveOrUpdate(source);
            logInfo(SUCCESS_MESSAGE_PROCESSING, source.getClass().getSimpleName(), source.getId());

            processEndEventMessage(source);

          } catch (Exception e) {
            logError(ERROR_MESSAGE_PROCESSING, className, e.getMessage());
          }
        });
      }
    } catch (Exception e) {
      logError(ERROR_MESSAGE_PARSING, className, e.getMessage());
      throw e;
    }
  }


  @SqsListener(value = "${sqs.queue.balance}")
  public void consumeBalance(String message) throws JsonProcessingException {
    String className = BalanceSource.class.getSimpleName();
    try {
      List<BalanceSource> balanceSources = parsingMessage.parseMessage(message,
          BalanceSource.class);

      if (balanceSources.isEmpty()) {
        log.warn("No balance sources found in the message: {}", message);

      } else {
        balanceSources.forEach(source -> {
          try {

            balancePersistence.saveOrUpdate(source);
            logInfo(SUCCESS_MESSAGE_PROCESSING, source.getClass().getSimpleName(), source.getId());

            processEndEventMessage(source);

          } catch (Exception e) {
            logError(ERROR_MESSAGE_PROCESSING, className, e.getMessage());
          }
        });
      }

    } catch (Exception e) {
      logError(ERROR_MESSAGE_PARSING, className, e.getMessage());
      throw e;
    }
  }

  public void processEndEventMessage(@NonNull BaseSource source) {
    if (source.getId().startsWith("end")) {
      boolean result = processEndEvent.processEndEvent();
      if (result) {
        log.info("End event processed successfully");
        processEndEvent.processAgreement();
      } else {
        log.warn("Waiting for end event to be processed ...");
      }
    }
  }

  private void logError(String message, Object... args) {
    log.error(message, args);
  }

  private void logInfo(String message, Object... args) {
    log.info(message, args);
  }


}
