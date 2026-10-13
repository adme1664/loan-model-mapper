package com.adme.learning.loan.model.mapper.creditfacility.boundary;

import com.adme.learning.model.generator.DealBundleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange("/api/v1/credit-facilities")
public interface CreditFacilityBundleResourceClient {
  @PostExchange("/bundle")
  ResponseEntity<Void> createAgreementBundle( @RequestBody DealBundleDTO cfBundleDto);

  @PutExchange("/bundle")
  ResponseEntity<Void> updateAgreementBundle(@RequestBody DealBundleDTO cfBundleDto);

  @DeleteExchange("/bundle")
  ResponseEntity<Void> purgeAgreementBundle(@RequestBody DealBundleDTO cfBundleDto);
}
