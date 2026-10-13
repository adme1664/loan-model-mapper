package com.adme.learning.loan.model.mapper.agreement.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Agreement MongoDB operations.
 * <p>
 * This boundary component provides data access operations for Agreement entities.
 * It serves as the entry point for persistence operations and can be consumed
 * by control layer components.
 */
@Repository
public interface AgreementRepository extends MongoRepository<Agreement, String> {

    /**
     * Finds an agreement by its business key (agmtId).
     *
     * @param agmtId the agreement identifier from the source system
     * @return an Optional containing the agreement if found
     */
    Optional<Agreement> findByAgmtId(String agmtId);

    /**
     * Finds all agreements for a specific batch.
     *
     * @param batchId the batch identifier
     * @return list of agreements in the batch
     */
    List<Agreement> findByBatchId(String batchId);

    /**
     * Finds all agreements from a specific source system.
     *
     * @param sourceSystem the source system identifier
     * @return list of agreements from the source system
     */
    List<Agreement> findBySourceSystem(String sourceSystem);

    /**
     * Finds agreements by business date.
     *
     * @param businessDt the business date
     * @return list of agreements for the given business date
     */
    List<Agreement> findByBusinessDt(String businessDt);

    /**
     * Finds agreements by operation type.
     *
     * @param operationType the operation type (I, U, or D)
     * @return list of agreements with the specified operation type
     */
    List<Agreement> findByOperationType(String operationType);

    /**
     * Checks if an agreement exists by agmtId.
     *
     * @param agmtId the agreement identifier
     * @return true if the agreement exists, false otherwise
     */
    boolean existsByAgmtId(String agmtId);

    /**
     * Deletes agreements by agmtId.
     *
     * @param agmtId the agreement identifier
     */
    void deleteByAgmtId(String agmtId);

    /**
     * Finds the most recent agreement for a given agmtId based on updatedAt timestamp.
     *
     * @param agmtId the agreement identifier
     * @return an Optional containing the most recent agreement if found
     */
    @Query("{ 'agmtId': ?0 }")
    Optional<Agreement> findLatestByAgmtId(String agmtId);


}