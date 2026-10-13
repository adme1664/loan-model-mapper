package com.adme.learning.loan.model.mapper.agreement.control;

import com.adme.learning.loan.model.mapper.agreement.boundary.AgreementBundleSource;
import com.adme.learning.loan.model.mapper.agreement.entity.AccrualInterest;
import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSource;
import com.adme.learning.loan.model.mapper.agreement.entity.BalanceSource;
import com.adme.learning.loan.model.mapper.agreement.entity.LoanBalanceType;
import com.adme.learning.loan.model.mapper.common.BundleContext;
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
  public abstract BalanceDTO toBalanceDTO(AccrualInterest dto, BundleContext context);

  public BalanceDTO mapToBalance(AgreementSource dto, BundleContext context) {
    if (Objects.isNull(dto) || Objects.isNull(dto.getAccrualInterest())) {
      return null;
    }
    AccrualInterest accrualInterest = dto.getAccrualInterest();
    BalanceDTO balanceDTO = toBalanceDTO(accrualInterest, context);
    balanceDTO.setBalanceType(LoanBalanceType.ACCRUAL_INTEREST.getValue());
    balanceDTO.setCreditAmount(accrualInterest.getAccrualInterestCumulatedAmt());
    balanceDTO.setDebitAmount(BigDecimal.ZERO);
    balanceDTO.setBalanceDate(CommonMapper.convertInstantToString(context.businessDate()));
   return balanceDTO;
  }

  public List<BalanceDTO> mapToList(BalanceSource source, BundleContext context) {

    List<BalanceDTO> balanceDTOS = new java.util.ArrayList<>();

    if ( Objects.isNull(source)) {
      return Collections.emptyList();
    }

    //BankGross Amount
    if (Objects.nonNull(source.getBankGrossAgmtAmt())) {
      BalanceDTO balanceDTO = new BalanceDTO();
      balanceDTO.setBalanceDate(CommonMapper.convertInstantToString(context.businessDate()));
      balanceDTO.setBalanceType(LoanBalanceType.BANK_GROSS.getValue());
      balanceDTO.setCreditAmount(source.getBankGrossAgmtAmt());
      balanceDTO.setDebitAmount(BigDecimal.ZERO);
      balanceDTOS.add(balanceDTO);
    }

    //CurrentAmnt
    if (Objects.nonNull(source.getGlobalCurrentAgmtAmt())) {
      BalanceDTO balanceDTO = new BalanceDTO();
      balanceDTO.setBalanceDate(CommonMapper.convertInstantToString(context.businessDate()));
      balanceDTO.setBalanceType(LoanBalanceType.CURRENT_ORIGINAL.getValue());
      balanceDTO.setCreditAmount(source.getGlobalCurrentAgmtAmt());
      balanceDTO.setDebitAmount(BigDecimal.ZERO);
      balanceDTOS.add(balanceDTO);
    }

    //BankNet Amount
    if (Objects.nonNull(source.getBankNetAgmtAmt())) {
      BalanceDTO balanceDTO = new BalanceDTO();
      balanceDTO.setBalanceDate(CommonMapper.convertInstantToString(context.businessDate()));
      balanceDTO.setBalanceType(LoanBalanceType.BANK_NET.getValue());
      balanceDTO.setCreditAmount(source.getBankNetAgmtAmt());
      balanceDTO.setDebitAmount(BigDecimal.ZERO);
      balanceDTOS.add(balanceDTO);
    }

    //Participant Amount
    if (Objects.nonNull(source.getParticipantAgmtAmt())) {
      BalanceDTO balanceDTO = new BalanceDTO();
      balanceDTO.setBalanceDate(CommonMapper.convertInstantToString(context.businessDate()));
      balanceDTO.setBalanceType(LoanBalanceType.PARTICIPANT.getValue());
      balanceDTO.setCreditAmount(source.getParticipantAgmtAmt());
      balanceDTO.setDebitAmount(BigDecimal.ZERO);
      balanceDTOS.add(balanceDTO);
    }
    return balanceDTOS;

  }

}
