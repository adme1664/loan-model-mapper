package com.adme.learning.loan.model.mapper.agreement.control;

import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSource;
import com.adme.learning.loan.model.mapper.common.BundleContext;
import com.adme.learning.model.generator.AgreementDTO;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class LoanAgreementMapper {

  @Mapping(target = "agreementType", source = "agreementSource.agmtTypeCd")
  @Mapping(target = "agreementNo", expression = "java(toAgreementNo(agreementSource, bundleContext))")
  @Mapping(target = "creditFacilityNo", source = "agreementSource.creditFacilityId")
  @Mapping(target = "productCode", source = "agreementSource.productCd")
  @Mapping(target = "statusCode", source = "agreementSource.statusCd")
  @Mapping(target = "statusCodeSource", source = "agreementSource.statusCdSrc")
  @Mapping(target = "unbankLetterIssuerIndicator", source = "agreementSource.unbnkLetterIssuerInd")
  @Mapping(target = "originalAmount", source = "agreementSource.originalAmt")
  @Mapping(target = "currency", source = "agreementSource.currency")
  @Mapping(target = "effectiveDate", ignore = true)
  @Mapping(target = "expiryDate", ignore = true)
  @Mapping(target = "maturityDate", ignore = true)
 abstract AgreementDTO toAgreementDTO(AgreementSource agreementSource,
      BundleContext bundleContext);

  public AgreementDTO mapToAgreement(AgreementSource agreementSource, BundleContext context) {
    AgreementDTO agreementDTO = toAgreementDTO(agreementSource, context);
    agreementDTO.setEffectiveDate(CommonMapper.convertInstantToString(agreementSource.getEffctvDt()));
    agreementDTO.setExpiryDate(CommonMapper.convertInstantToString(agreementSource.getExpiryDt()));
    agreementDTO.setMaturityDate(CommonMapper.convertInstantToString(agreementSource.getMaturityDt()));
    return agreementDTO;
  }


   String toAgreementNo(AgreementSource source,
      BundleContext bundleContext) {
    if (Objects.nonNull(source.getAgmtId())) {
      return source.getAgmtId();
    } else {
      return bundleContext.id();
    }
  }


}
