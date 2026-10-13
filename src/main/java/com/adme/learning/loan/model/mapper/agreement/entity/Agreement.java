package com.adme.learning.loan.model.mapper.agreement.entity;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB document entity for storing loan agreement data.
 * <p>
 * This entity stores AgreementSource data as a MongoDB document, maintaining
 * the complete agreement information including metadata for tracking changes
 * and business operations.
 */
@Document(collection = "agreements")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class Agreement extends BaseEntity {
    /**
     * Agreement identifier from the source system.
     * This is the business key used to identify the agreement.
     */
    private String agmtId;

    /**
     * Complete agreement source data.
     * Stores the full AgreementSource object as a nested document.
     */
    private AgreementSource agreementSource;

    /**
     * Business date when the agreement was processed.
     */
    private String businessDt;

    /**
     * Batch identifier for tracking data processing batches.
     */
    private String batchId;

    /**
     * Source system that originated the agreement data.
     */
    private String sourceSystem;

    /**
     * Operation type: I (insert), U (update), D (delete).
     */
    private String operationType;

    /**
     * Event name associated with this agreement change.
     */

    /**
     * Creates a new Agreement entity from an AgreementSource.
     *
     * @param source the source agreement data
     * @return a new Agreement entity with populated fields
     */
    public static Agreement fromSource(AgreementSource source) {
        return Agreement.builder()
                .agmtId(source.getAgmtId())
                .agreementSource(source)
                .businessDt(source.getBusinessDate())
                .sourceSystem(source.getSourceSystem())
                .eventName(source.getEventName())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    /**
     * Updates this Agreement entity with new source data.
     *
     * @param source the new source agreement data
     */
    public void updateFromSource(AgreementSource source) {
        this.agreementSource = source;
        this.businessDt = source.getBusinessDate();
        this.sourceSystem = source.getSourceSystem();
        this.eventName = source.getEventName();
        this.updatedAt = Instant.now();
    }
}
