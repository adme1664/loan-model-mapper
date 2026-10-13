package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.model.generator.AgreementBundleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;


@HttpExchange("/api/v1/agreements")
public interface AgreementBundleResourceClient {

  @PostExchange("/bundle")
  ResponseEntity<Void> createAgreementBundle( @RequestBody AgreementBundleDTO agreementBundleResource);

  @PutExchange("/bundle")
  ResponseEntity<Void> updateAgreementBundle(@RequestBody AgreementBundleDTO agreementBundleResource);

  @DeleteExchange("/bundle")
  ResponseEntity<Void> purgeAgreementBundle(@RequestBody AgreementBundleDTO agreementBundleResource);

}
