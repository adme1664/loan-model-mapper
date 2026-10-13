package com.adme.learning.loan.model.mapper.creditfacility.control;

import com.adme.learning.loan.model.mapper.creditfacility.entity.BorrowingParty;
import com.adme.learning.model.generator.CfPartyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR)
public interface CfPartyMapper {

  @Mapping(target = "unbankId", source = "dto.unbnkId")
  @Mapping(target = "memberIdNo", source = "dto.memberIdNo")
  CfPartyDTO mapTo(BorrowingParty dto);
}
