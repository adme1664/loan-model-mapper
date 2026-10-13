package com.adme.learning.loan.model.mapper.agreement.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB document entity for storing agreement schedule data.
 * <p>
 * This entity stores ScheduleAgreement data as a MongoDB document, maintaining the schedule
 * information for individual agreements including metadata for tracking changes and business
 * operations.
 */
@Document(collection = "agreement_schedules")
@Setter
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class AgreementSchedule extends BaseEntity {


  /**
   * Agreement identifier from the source system. This is the business key used to identify the
   * schedule for a specific agreement.
   */
  private String agmtId;


  /**
   * Complete schedule agreement data. Stores the full ScheduleAgreement object as a nested
   * document.
   */
  private ScheduleInfo scheduleInfo;


  /**
   * Creates a new AgreementSchedule entity from a ScheduleAgreement.
   *
   * @param scheduleSource the source schedule agreement data
   * @return a new AgreementSchedule entity with populated fields
   */
  public static AgreementSchedule fromSource(ScheduleSource scheduleSource) {
    ScheduleInfo scheduleInfo = null;

    if (scheduleSource.getSchedulesInfo() != null && !scheduleSource.getSchedulesInfo().isEmpty()) {
      scheduleInfo = scheduleSource.getSchedulesInfo().getFirst();
    }
    return AgreementSchedule.builder()
        .agmtId(scheduleSource.getId())
        .scheduleInfo(scheduleInfo)
        .eventName(scheduleSource.getEventName())
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();
  }

  /**
   * Updates this AgreementSchedule entity with new source data.
   *
   * @param scheduleSource the new source schedule agreement data
   */
  public void updateFromSource(ScheduleSource scheduleSource) {
    if (scheduleSource.getId().startsWith("end")) {
      this.scheduleInfo = null;
    } else {
      this.scheduleInfo = scheduleSource.getSchedulesInfo().getFirst();
    }
    this.eventName = scheduleSource.getEventName();
    this.updatedAt = Instant.now();
  }
}