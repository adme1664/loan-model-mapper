package com.adme.learning.loan.model.mapper.agreement.entity;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB document entity for storing balance data.
 * <p>
 * This entity stores BalanceSource data as a MongoDB document, maintaining
 * the balance information for agreements including metadata for
 * tracking changes and business operations.
 */
@Document(collection = "balances")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Balance extends BaseEntity {


    /**
     * Agreement identifier from the source system.
     * This is the business key used to identify the balance for a specific agreement.
     */
    private String agmtId;

    /**
     * Complete balance source data.
     * Stores the full BalanceSource object as a nested document.
     */
    private BalanceSource balanceSource;


    /**
     * Creates a new Balance entity from a BalanceSource.
     *
     * @param balanceSource the source balance data
     * @param eventName the event name
     * @return a new Balance entity with populated fields
     */
    public static Balance fromSource(BalanceSource balanceSource, String eventName) {
        return Balance.builder()
                .agmtId(balanceSource.getAgmtId() != null ? balanceSource.getAgmtId() : null)
                .balanceSource(balanceSource)
                .eventName(eventName)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    /**
     * Updates this Balance entity with new source data.
     *
     * @param balanceSource the new source balance data
     * @param eventName the event name
     */
    public void updateFromSource(BalanceSource balanceSource, String eventName) {
        this.balanceSource = balanceSource;
        this.eventName = eventName;
        this.updatedAt = Instant.now();
    }
}