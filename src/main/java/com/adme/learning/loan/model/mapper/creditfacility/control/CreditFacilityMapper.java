package com.adme.learning.loan.model.mapper.creditfacility.control;

import com.adme.learning.loan.model.mapper.creditfacility.entity.CreditFacility;
import com.adme.learning.model.generator.CreditFacilityDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CreditFacilityMapper {

  @Mapping(target = "creditFacilityNo", source = "creditFacilityId")
  @Mapping(target = "creditFacilityName", source = "creditFacilityName")
  @Mapping(target = "statusCode", source = "statusCd")
  @Mapping(target = "statusCodeSource", source = "statusCdSrc")
  @Mapping(target = "productCode", source = "productCd")
  @Mapping(target = "lenderRoleCode", source = "lenderRoleCd")
  @Mapping(target = "effectiveDate", source = "effctvDt")
  @Mapping(target = "expiryDate", source = "expiryDt")
  @Mapping(target = "maturityDate", source = "maturityDt")
  @Mapping(target = "currency", source = "currency")
  CreditFacilityDTO mapTo(CreditFacility source);

}
