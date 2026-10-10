package com.adme.learning.loan.model.mapper.agreement.control;

import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSource;
import com.adme.learning.model.generator.AgreementDTO;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface LoanAgreementMapper {

  @Mapping(target = "agreementType", source = "agreementSource.agmtTypeCd")
  @Mapping(target = "agreementNo", expression = "java(toAgreementNo(agreementSource, agreementBundleContext))")
  @Mapping(target = "creditFacilityNo", source = "agreementSource.creditFacilityId")
  @Mapping(target = "productCode", source = "agreementSource.productCd")
  @Mapping(target = "statusCode", source = "agreementSource.statusCd")
  @Mapping(target = "statusCodeSource", source = "agreementSource.statusCdSrc")
  @Mapping(target = "unbankLetterIssuerIndicator", source = "agreementSource.unbnkLetterIssuerInd")
  @Mapping(target = "originalAmount", source = "agreementSource.originalAmt")
  @Mapping(target = "currency", source = "agreementSource.currency")
  @Mapping(target = "effectiveDate", source = "agreementSource.effctvDt")
  @Mapping(target = "expiryDate", source = "agreementSource.expiryDt")
  @Mapping(target = "maturityDate", source = "agreementSource.maturityDt")
  AgreementDTO toAgreementDTO(AgreementSource agreementSource,
      AgreementBundleContext agreementBundleContext);


  default String toAgreementNo(AgreementSource source,
      AgreementBundleContext agreementBundleContext) {
    if (Objects.nonNull(source.getAgmtId())) {
      return source.getAgmtId();
    } else {
      return agreementBundleContext.id();
    }
  }
}
