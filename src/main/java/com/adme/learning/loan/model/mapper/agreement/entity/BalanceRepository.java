package com.adme.learning.loan.model.mapper.agreement.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Balance MongoDB operations.
 * <p>
 * This boundary component provides data access operations for Balance entities.
 * It serves as the entry point for persistence operations and can be consumed
 * by control layer components.
 */
@Repository
public interface BalanceRepository extends MongoRepository<Balance, String> {

    /**
     * Searches for balance by agreement identifier.
     *
     * @param agmtId the agreement identifier
     * @return an Optional containing the balance if found
     */
    Optional<Balance> searchByAgmtId(String agmtId);

    /**
     * Finds a single balance by agmtId.
     *
     * @param agmtId the agreement identifier
     * @return an Optional containing the balance if found
     */
    Optional<Balance> findByAgmtId(String agmtId);

    /**
     * Checks if a balance exists by agmtId.
     *
     * @param agmtId the agreement identifier
     * @return true if the balance exists, false otherwise
     */
    boolean existsByAgmtId(String agmtId);

    /**
     * Deletes balance by agmtId.
     *
     * @param agmtId the agreement identifier
     */
    void deleteByAgmtId(String agmtId);
}