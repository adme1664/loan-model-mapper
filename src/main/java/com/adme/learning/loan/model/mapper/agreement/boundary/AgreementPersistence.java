package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.loan.model.mapper.agreement.entity.AgreementRepository;
import com.adme.learning.loan.model.mapper.agreement.entity.Agreement;
import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Control layer component for agreement persistence operations.
 * <p>
 * This component provides business logic for saving, updating, and retrieving agreement data. It
 * coordinates between the boundary layer (repository) and external consumers.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AgreementPersistence implements LoanPersistence<Agreement, AgreementSource> {

  private final AgreementRepository agreementRepository;

  /**
   * Saves or updates an agreement from source data.
   * <p>
   * If an agreement with the same agmtId already exists, it will be updated. Otherwise, a new
   * agreement will be created.
   *
   * @param source the agreement source data to persist
   */
  @Override
  public void saveOrUpdate(AgreementSource source) {
    log.debug("Saving or updating agreement with agmtId: {}", source.getAgmtId());

    Optional<Agreement> existing = agreementRepository.findByAgmtId(source.getAgmtId());

    if (existing.isPresent()) {
      Agreement agreement = existing.get();
      agreement.updateFromSource(source);
      log.info("Updating existing agreement: {}", source.getAgmtId());
      agreementRepository.save(agreement);
    } else {
      Agreement newAgreement = Agreement.fromSource(source);
      log.info("Creating new agreement: {}", source.getAgmtId());
      agreementRepository.save(newAgreement);
    }
  }

  /**
   * Retrieves an agreement by its business key.
   *
   * @param agmtId the agreement identifier
   * @return an Optional containing the agreement if found
   */
  @Override
  public Optional<Agreement> findById(String agmtId) {
    log.debug("Finding agreement with agmtId: {}", agmtId);
    return agreementRepository.findByAgmtId(agmtId);
  }

  /**
   * Deletes an agreement by its business key.
   *
   * @param agmtId the agreement identifier
   */
  @Override
  public void deleteById(String agmtId) {
    log.info("Deleting agreement with agmtId: {}", agmtId);
    agreementRepository.deleteByAgmtId(agmtId);
  }

  /**
   * Checks if an agreement exists.
   *
   * @param agmtId the agreement identifier
   * @return true if the agreement exists, false otherwise
   */
  public boolean exists(String agmtId) {
    return agreementRepository.existsByAgmtId(agmtId);
  }
}