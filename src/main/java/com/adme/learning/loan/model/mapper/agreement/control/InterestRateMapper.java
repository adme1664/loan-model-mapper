package com.adme.learning.loan.model.mapper.agreement.control;

import com.adme.learning.loan.model.mapper.agreement.entity.CreditInterest;
import com.adme.learning.loan.model.mapper.agreement.entity.InterestRateType;
import com.adme.learning.model.generator.InterestRateDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class InterestRateMapper {

  @Mapping(target = "agreementId", ignore = true)
  @Mapping(target = "interestRateId", ignore = true)
  public abstract InterestRateDTO interestRateDTO(CreditInterest dto, AgreementBundleContext context);

  public List<InterestRateDTO> mapToList(CreditInterest dto, AgreementBundleContext context) {
    if(dto == null) {
      return Collections.emptyList();
    }

    List<InterestRateDTO> interestRateDTOS = new ArrayList<>();
    if(dto.getInterestRate() != null) {
      InterestRateDTO interestRateDTO = interestRateDTO(dto, context);
      interestRateDTO.setInterestRateType(InterestRateType.FIXED.getValue());
      interestRateDTO.setInterestRate(dto.getInterestRate());
      interestRateDTO.setEffectiveDate(dto.getEffctvDt());
      interestRateDTOS.add(interestRateDTO);
    }

    if (dto.getBaseRate() != null || dto.getSpreadRate() != null) {
      InterestRateDTO interestRateDTO = interestRateDTO(dto, context);
      interestRateDTO.setInterestRateType(InterestRateType.VARIABLE.getValue());
      interestRateDTO.setEffectiveDate(dto.getEffctvDt());
      interestRateDTO.setSpreadRate(dto.getSpreadRate());
      interestRateDTOS.add(interestRateDTO);
    }

    return interestRateDTOS;
  }

}
