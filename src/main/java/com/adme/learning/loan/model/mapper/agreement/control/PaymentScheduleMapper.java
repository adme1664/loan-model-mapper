package com.adme.learning.loan.model.mapper.agreement.control;

import com.adme.learning.loan.model.mapper.agreement.boundary.AgreementBundleSource;
import com.adme.learning.loan.model.mapper.agreement.entity.RepymtLineItem;
import com.adme.learning.loan.model.mapper.common.BundleContext;
import com.adme.learning.model.generator.PaymentScheduleDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PaymentScheduleMapper {

  @Mapping(target = "paymentAmnt", source = "repymtAmt")
  @Mapping(target = "paymentScheduleId", ignore = true)
  @Mapping(target = "agreementId", ignore = true)
  abstract PaymentScheduleDTO toPaymentScheduleDTO(RepymtLineItem item);

  public List<PaymentScheduleDTO> mapToList(AgreementBundleSource source,  BundleContext context) {

    if (Objects.isNull(source.getSchedule())) {
      return Collections.emptyList();
    }

    if (Objects.isNull(source.getSchedule().getScheduleInfo())) {
      return Collections.emptyList();
    }

    if (source.getSchedule().getScheduleInfo().getRepymtLineItem().isEmpty()) {
      return Collections.emptyList();
    }
    List<PaymentScheduleDTO> paymentScheduleDTOS = new ArrayList<>();

    List<RepymtLineItem> items = source.getSchedule().getScheduleInfo().getRepymtLineItem();

    for (RepymtLineItem item : items) {
      PaymentScheduleDTO paymentScheduleDTO = toPaymentScheduleDTO(item);
      paymentScheduleDTO.setBusinessDate(CommonMapper.convertInstantToString(
          context.businessDate()));
      paymentScheduleDTO.setPaymentDate(CommonMapper.convertInstantToString(item.getRepymtDate()));
      paymentScheduleDTOS.add(paymentScheduleDTO);
    }
    return paymentScheduleDTOS;
  }

}
