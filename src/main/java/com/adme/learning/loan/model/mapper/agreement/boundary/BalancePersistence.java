package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.loan.model.mapper.agreement.entity.Balance;
import com.adme.learning.loan.model.mapper.agreement.entity.BalanceRepository;
import com.adme.learning.loan.model.mapper.agreement.entity.BalanceSource;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Boundary layer component for balance persistence operations.
 * <p>
 * This component provides business logic for saving, updating, and retrieving balance data. It
 * coordinates between the entity layer (repository) and external consumers.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BalancePersistence implements LoanPersistence<Balance, BalanceSource> {

  private final BalanceRepository balanceRepository;

  /**
   * Updates an existing balance or creates a new one if it doesn't exist.
   * <p>
   * If a balance with the same agmtId already exists, it will be updated. Otherwise, a new balance
   * will be created.
   *
   * @param balanceSource the balance source data to persist
   */
  @Override
  public void saveOrUpdate(BalanceSource balanceSource) {
    String agmtId =
        balanceSource.getAgmtId() != null ? balanceSource.getAgmtId() : null;

    String eventName = balanceSource.getEventName();
    log.debug("Updating balance with agmtId: {}", agmtId);

    Optional<Balance> existing = balanceRepository.searchByAgmtId(agmtId);

    if (existing.isPresent()) {
      Balance balance = existing.get();
      balance.updateFromSource(balanceSource, eventName);
      log.info("Updating existing balance: {}", agmtId);
      balanceRepository.save(balance);
    } else {
      Balance newBalance = Balance.fromSource(balanceSource, eventName);
      log.info("Creating new balance: {}", agmtId);
      balanceRepository.save(newBalance);
    }
  }

  /**
   * Searches for a balance by agreement identifier.
   *
   * @param agmtId the agreement identifier
   * @return an Optional containing the balance if found
   */
  @Override
  public Optional<Balance> findById(String agmtId) {
    log.debug("Searching for balance with agmtId: {}", agmtId);
    return balanceRepository.findByAgmtId(agmtId);
  }



  /**
   * Deletes a balance by its business key.
   *
   * @param agmtId the agreement identifier
   */
  @Override
  public void deleteById(String agmtId) {
    log.info("Deleting balance with agmtId: {}", agmtId);
    balanceRepository.deleteByAgmtId(agmtId);
  }

  /**
   * Checks if a balance exists.
   *
   * @param agmtId the agreement identifier
   * @return true if the balance exists, false otherwise
   */
  public boolean exists(String agmtId) {
    return balanceRepository.existsByAgmtId(agmtId);
  }

}