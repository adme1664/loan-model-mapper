package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.loan.model.mapper.agreement.entity.Agreement;
import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSchedule;
import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSource;
import com.adme.learning.loan.model.mapper.agreement.entity.Balance;
import com.adme.learning.loan.model.mapper.agreement.entity.BalanceSource;
import com.adme.learning.loan.model.mapper.agreement.entity.ScheduleSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Boundary component for processing end events.
 * <p>
 * This component manages the mapping between end event types and their corresponding persistence
 * handlers, providing a centralized way to check event processing status.
 */
@Component
@Slf4j
public class ProcessEndEvent {

  private final Map<EndEvent, LoanPersistence<?, ?>> endEventMap;
  private final ProcessingAgreement processingAgreement;
  private final AgreementPersistence agreementPersistence;
  private final BalancePersistence balancePersistence;
  private final AgreementSchedulePersistence schedulePersistence;


  /**
   * Constructs the ProcessEndEvent with required persistence handlers.
   *
   * @param agreementPersistence the agreement persistence handler
   * @param balancePersistence   the balance persistence handler
   * @param schedulePersistence  the schedule persistence handler
   * @param processingAgreement  the agreement processing service
   */
  public ProcessEndEvent(AgreementPersistence agreementPersistence,
      BalancePersistence balancePersistence,
      AgreementSchedulePersistence schedulePersistence,
      ProcessingAgreement processingAgreement, BalancePersistence balancePersistence1,
      AgreementSchedulePersistence schedulePersistence1) {
    this.processingAgreement = processingAgreement;
    this.agreementPersistence = agreementPersistence;
    this.balancePersistence = balancePersistence1;
    this.schedulePersistence = schedulePersistence1;

    this.endEventMap = Map.of(
        EndEvent.END_AGREEMENT, agreementPersistence,
        EndEvent.END_BALANCE, balancePersistence,
        EndEvent.END_SCHEDULE, schedulePersistence
    );
  }

  /**
   * Processes all registered end events and checks if any have been processed.
   * <p>
   * Iterates through all registered end events and checks if the corresponding entity exists in the
   * persistence layer using the event's value as the identifier.
   *
   * @return true if at least one end event has been processed (entity exists), false otherwise
   */
  public boolean processEndEvent() {
    return endEventMap.entrySet().stream()
        .anyMatch(entry -> {
          EndEvent endEvent = entry.getKey();
          LoanPersistence<?, ?> persistence = entry.getValue();
          boolean exists = persistence.exists(endEvent.getValue());

          if (exists) {
            log.debug("End event {} has been processed", endEvent);
          }

          return exists;
        });
  }

  /**
   * Processes agreements from the database.
   * <p>
   * Fetches up to 20 agreements from the database and processes each one by sending them to the
   * agreement processing service.
   */
  public void processAgreement() {
    log.info("Start fetching agreementBundle from database ...");
    int page = 0;
    int size = 20;
    List<Agreement> agreements;
    do {
      agreements = agreementPersistence.getAgreements(page, size);

      if (!agreements.isEmpty()) {
        agreements.forEach(agreement -> {
          if (!agreement.getAgmtId().startsWith("end")) {
            AgreementBundleSource agreementBundleSource = AgreementBundleSource.builder().build();

            AgreementSource agreementSource = agreement.getAgreementSource();
            agreementBundleSource.setAgreement(agreementSource);

            //Get the balance
            Optional<Balance> balance = balancePersistence.findById(agreement.getAgmtId());
            if (balance.isPresent()) {
              BalanceSource balanceSource = balance.get().getBalanceSource();
              agreementBundleSource.setBalance(balanceSource);
            }

            //Get the schedule
            Optional<AgreementSchedule> optionalScheduleSource = schedulePersistence.findById(
                agreement.getAgmtId());
            optionalScheduleSource.ifPresent(agreementBundleSource::setSchedule);
            processingAgreement.processAgreement(agreementBundleSource);
          }
        });
      }
      page++;
    } while (!agreements.isEmpty());

  }

  public void allEndEvent(){
    endEventMap.forEach((key, value) -> {
      value.deleteById(key.getValue());
    });
  }
}
