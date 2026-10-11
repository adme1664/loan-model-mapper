package com.adme.learning.loan.model.mapper.agreement.boundary;

import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcessEndEvent {
  private final AgreementPersistence agreementPersistence;
  private final BalancePersistence balancePersistence;
  private final AgreementSchedulePersistence schedulePersistence;

  private final HashMap<String, EndEvent> endEventMap = new HashMap<>();

  public ProcessEndEvent(AgreementPersistence agreementPersistence, BalancePersistence balancePersistence,
      AgreementSchedulePersistence schedulePersistence) {
    this.agreementPersistence = agreementPersistence;
    this.balancePersistence = balancePersistence;
    this.schedulePersistence = schedulePersistence;
  }

  boolean processEndEvent(String endEvent, String eventName) {
    // Implementation for processing end event
    return true;
  }

}
