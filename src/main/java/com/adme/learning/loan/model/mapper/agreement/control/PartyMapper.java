package com.adme.learning.loan.model.mapper.agreement.control;

import com.adme.learning.loan.model.mapper.agreement.entity.Party;
import com.adme.learning.model.generator.PartyDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PartyMapper {

  @Mapping(target = "partyId", ignore = true)
  @Mapping(target = "agreementId", ignore = true)
  @Mapping(target = "unbankId", source = "dto.unbnkId")
  @Mapping(target = "memberIdNo", source = "dto.memberIdNo")
  PartyDTO partyDTO(Party dto);

}
