package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSchedule;
import com.adme.learning.loan.model.mapper.agreement.entity.AgreementScheduleRepository;
import com.adme.learning.loan.model.mapper.agreement.entity.ScheduleSource;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Boundary layer component for schedule persistence operations.
 * <p>
 * This component provides business logic for saving, updating, and retrieving schedule data. It
 * coordinates between the entity layer (repository) and external consumers.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AgreementSchedulePersistence implements LoanPersistence<AgreementSchedule, ScheduleSource> {

  private final AgreementScheduleRepository scheduleRepository;

  /**
   * Saves or updates a schedule from source data.
   * <p>
   * If a schedule with the same agmtId already exists, it will be updated. Otherwise, a new
   * schedule will be created.
   *
   * @param source the schedule source data to persist
   */
  @Override
  public void saveOrUpdate(ScheduleSource source) {

    log.debug("Saving or updating schedule with agreementId: {}", source.getId());

    Optional<AgreementSchedule> existing = scheduleRepository.searchByAgmtId(source.getId());

    if (existing.isPresent()) {
      AgreementSchedule schedule = existing.get();
      schedule.updateFromSource(source);
      log.info("Updating existing schedule: {}", source.getId());
      scheduleRepository.save(schedule);
    } else {
      AgreementSchedule newSchedule = AgreementSchedule.fromSource(source);
      log.info("Creating new schedule: {}", source.getId());
      scheduleRepository.save(newSchedule);
    }
  }

  /**
   * Retrieves a schedule by its business key.
   *
   * @param agmntId the credit facility identifier
   * @return an Optional containing the schedule if found
   */
  @Override
  public Optional<AgreementSchedule> findById(String agmntId) {
    log.debug("Finding schedule with agmtId: {}", agmntId);
    return scheduleRepository.findByAgmtId(agmntId);
  }

  @Override
  public void deleteById(String agmntId) {
    log.info("Deleting schedule with agmtId: {}", agmntId);
    scheduleRepository.deleteByAgmtId(agmntId);
  }


  /**
   * Checks if a schedule exists.
   *
   * @param agmntId the credit facility identifier
   * @return true if the schedule exists, false otherwise
   */
  public boolean exists(String agmntId) {
    return scheduleRepository.existsByAgmtId(agmntId);
  }
}