package com.adme.learning.loan.model.mapper.creditfacility.control;


import com.adme.learning.loan.model.mapper.creditfacility.entity.DealSource;
import com.adme.learning.loan.model.mapper.common.BundleContext;
import com.adme.learning.model.generator.DealDTO;
import java.util.Objects;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DealMapper {


  @Mapping(target = "borrowingAgreementId", expression = "java(toDealNo(source, context))")
  @Mapping(target = "borrowingAgreementName", source = "source.borrowingAgmtName")
  @Mapping(target = "effectiveDate", source = "source.effctvDt")
  @Mapping(target = "classificationCode", source = "source.classificationCd")
  @Mapping(target = "nextReviewDate", source = "source.nextReviewDt")
  DealDTO mapTo(DealSource source, BundleContext context);

  default String toDealNo(DealSource source, BundleContext context) {
    if (Objects.nonNull(source.getBorrowingAgmtId())) {
      return source.getBorrowingAgmtId();
    } else {
      return context.id();
    }
  }

}
