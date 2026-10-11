package com.adme.learning.loan.model.mapper.agreement.control;

import com.adme.learning.loan.model.mapper.agreement.entity.AccrualInterest;
import com.adme.learning.loan.model.mapper.agreement.entity.LoanBalanceType;
import com.adme.learning.model.generator.BalanceDTO;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class BalanceMapper {

  @Mapping(target = "agreementId", ignore = true)
  @Mapping(target = "balanceId", ignore = true)
  public abstract BalanceDTO toBalanceDTO(AccrualInterest dto, AgreementBundleContext context);


  public List<BalanceDTO> mapToList(AccrualInterest dto, AgreementBundleContext context) {
    if(Objects.isNull(dto)) {
      return Collections.emptyList();
    }

    List<BalanceDTO> balanceDTOS = new java.util.ArrayList<>();
    BalanceDTO balanceDTO = toBalanceDTO(dto, context);
    balanceDTO.setBalanceType(LoanBalanceType.ACCRUAL_INTEREST.getValue());
    balanceDTO.setCreditAmount(dto.getAccrualInterestCumulatedAmt());
    balanceDTO.setDebitAmount(BigDecimal.ZERO);
    balanceDTO.setBalanceDate(CommonMapper.convertInstantToString(context.businessDate()));
    balanceDTOS.add(balanceDTO);
    return balanceDTOS;
  }

}
