package com.adme.learning.loan.model.mapper.creditfacility.control;

import com.adme.learning.loan.model.mapper.creditfacility.entity.AgreementReference;
import com.adme.learning.model.generator.AgreementRelationDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AgreementRelationMapper {

  @Mapping(target = "agreementId", source = "agreementReference.agmtId")
  AgreementRelationDTO mapTo(AgreementReference agreementReference);
}
