package com.adme.learning.loan.model.mapper.agreement.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AgreementSchedule MongoDB operations.
 * <p>
 * This boundary component provides data access operations for AgreementSchedule entities.
 * It serves as the entry point for persistence operations and can be consumed
 * by control layer components.
 */
@Repository
public interface AgreementScheduleRepository extends MongoRepository<AgreementSchedule, String> {

    /**
     * Searches for agreement schedules by agreement identifier.
     *
     * @param agmtId the agreement identifier
     * @return list of agreement schedules matching the agmtId
     */
    Optional<AgreementSchedule> searchByAgmtId(String agmtId);

    /**
     * Finds a single agreement schedule by agmtId.
     *
     * @param agmtId the agreement identifier
     * @return an Optional containing the agreement schedule if found
     */
    Optional<AgreementSchedule> findByAgmtId(String agmtId);


    /**
     * Checks if an agreement schedule exists by agmtId.
     *
     * @param agmtId the agreement identifier
     * @return true if the agreement schedule exists, false otherwise
     */
    boolean existsByAgmtId(String agmtId);

    /**
     * Deletes agreement schedules by agmtId.
     *
     * @param agmtId the agreement identifier
     */
    void deleteByAgmtId(String agmtId);
}