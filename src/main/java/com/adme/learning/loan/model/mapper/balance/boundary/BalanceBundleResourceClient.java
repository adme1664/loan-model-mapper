package com.adme.learning.loan.model.mapper.balance.boundary;

import com.adme.learning.model.generator.BalanceBundleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/balances")
public interface BalanceBundleResourceClient {
  @PostExchange("/bundle")
  ResponseEntity<Void> createBalanceBundle( @RequestBody BalanceBundleDTO balanceBundleDTO);
}
