package com.adme.learning.loan.model.mapper.balance.boundary;

import com.adme.learning.model.generator.BalanceBundleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Service
@Slf4j
public class ProcessingBalance {

  private final BalanceBundleResourceClient balanceBundleResourceClient;


  public ProcessingBalance(@Value("${agreement.api.base-url}") String techApiUrl) {

    //set the resource client
    WebClient agreementWebClient = WebClient.builder().baseUrl(techApiUrl).build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(agreementWebClient))
        .build();
    this.balanceBundleResourceClient = factory.createClient(BalanceBundleResourceClient.class);
  }

  public void processBalance(BalanceBundleDTO balanceBundleDTO) {
    log.info("Start processing balance for agreement ={} in Near Real Time", balanceBundleDTO.getAgreementNo());
    this.balanceBundleResourceClient.createBalanceBundle(balanceBundleDTO);
    log.info("Processing balance succeeded");
  }
}
