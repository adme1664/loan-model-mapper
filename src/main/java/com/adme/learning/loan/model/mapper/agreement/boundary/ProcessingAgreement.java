package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.loan.model.mapper.agreement.control.BalanceMapper;
import com.adme.learning.loan.model.mapper.agreement.control.InterestRateMapper;
import com.adme.learning.loan.model.mapper.agreement.control.LoanAgreementMapper;
import com.adme.learning.loan.model.mapper.agreement.control.PartyMapper;
import com.adme.learning.loan.model.mapper.agreement.control.PaymentScheduleMapper;
import com.adme.learning.loan.model.mapper.agreement.entity.AgreementSource;
import com.adme.learning.loan.model.mapper.agreement.entity.OperationType;
import com.adme.learning.loan.model.mapper.common.BundleContext;
import com.adme.learning.model.generator.AgreementBundleDTO;
import com.adme.learning.model.generator.BalanceDTO;
import com.adme.learning.model.generator.PaymentScheduleDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Service
@Slf4j
public class ProcessingAgreement {

  private final LoanAgreementMapper loanAgreementMapper;
  private final BalanceMapper balanceMapper;
  private final InterestRateMapper interestRateMapper;
  private final PartyMapper partyMapper;
  private final PaymentScheduleMapper paymentScheduleMapper;
  private final AgreementBundleResourceClient bundleApiClient;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public ProcessingAgreement(LoanAgreementMapper loanAgreementMapper, BalanceMapper balanceMapper,
      InterestRateMapper interestRateMapper, PartyMapper partyMapper,
      PaymentScheduleMapper paymentScheduleMapper,
      @Value("${agreement.api.base-url}") String agreementApiBaseUrl) {
    this.loanAgreementMapper = loanAgreementMapper;
    this.balanceMapper = balanceMapper;
    this.interestRateMapper = interestRateMapper;
    this.partyMapper = partyMapper;
    this.paymentScheduleMapper = paymentScheduleMapper;

    //set the resource client
    WebClient agreementWebClient = WebClient.builder().baseUrl(agreementApiBaseUrl).build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(agreementWebClient))
        .build();
    this.bundleApiClient = factory.createClient(AgreementBundleResourceClient.class);
  }

  void processAgreement() {
    log.info("Start fetching agreement ...");

  }

  void processAgreement(AgreementBundleSource source) {
    AgreementSource agreementSource = source.getAgreement();
    try {

      BundleContext context = new BundleContext(agreementSource.getId(),
          agreementSource.getEventName(), agreementSource.getBusinessDate());

      AgreementBundleDTO agreementBundleDTO = prepareAgreementBundle(
          source, context);

      log.info("Processing agreement with AgreementNo={}, AgreementType={}, Operation={}",
          agreementSource.getId(), agreementSource.getAgmtTypeCd(), agreementSource.getEventName());

      synchronize(agreementBundleDTO);

      log.info(
          "Agreement with AgreementNo={}, AgreementType={}, Operation={} with successfully processed.",
          agreementSource.getId(), agreementSource.getAgmtTypeCd(), agreementSource.getEventName());
    } catch (Exception e) {
      log.error(
          "Error processing agreement with AgreementNo={}, AgreementType={}, Operation={}: {}",
          agreementSource.getId(), agreementSource.getAgmtTypeCd(), agreementSource.getEventName(), e.getMessage());
    }
  }

  AgreementBundleDTO prepareAgreementBundle(AgreementBundleSource bundleSource,
      BundleContext context) {
    AgreementBundleDTO agreementBundleDTO = new AgreementBundleDTO();

    AgreementSource agreementSource = bundleSource.getAgreement();

    agreementBundleDTO.setAgreement(loanAgreementMapper.mapToAgreement(agreementSource, context));

    agreementBundleDTO.addBalancesItem(balanceMapper.mapToBalance(agreementSource, context));

    List<BalanceDTO> balanceDTOS = balanceMapper.mapToList(bundleSource, context);
    agreementBundleDTO.getBalances().addAll(balanceDTOS);

    agreementBundleDTO.setInterestRates(
        interestRateMapper.mapToList(agreementSource.getCreditInterest(), context));

    agreementBundleDTO.setParty(partyMapper.partyDTO(agreementSource.getParty()));

    List<PaymentScheduleDTO> paymentScheduleDTOS = paymentScheduleMapper.mapToList(bundleSource,context);

    agreementBundleDTO.setPaymentSchedules(paymentScheduleDTOS);

    agreementBundleDTO.setAgreementNo(context.id());
    agreementBundleDTO.setOperation(context.eventName());
    return agreementBundleDTO;
  }

  void synchronize(AgreementBundleDTO agreementBundleDTO) throws JsonProcessingException {
    if (OperationType.INSERT.getValue().equals(agreementBundleDTO.getOperation())) {
      var response = bundleApiClient.createAgreementBundle(agreementBundleDTO);
      processResponse(response, agreementBundleDTO);
    } else if (OperationType.MODIFY.getValue().equals(agreementBundleDTO.getOperation())) {
      var response = bundleApiClient.updateAgreementBundle(agreementBundleDTO);
      processResponse(response, agreementBundleDTO);
    } else if (OperationType.REMOVE.getValue().equals(agreementBundleDTO.getOperation())) {
      var response = bundleApiClient.purgeAgreementBundle(agreementBundleDTO);
      processResponse(response, agreementBundleDTO);
    } else {
      log.warn("Unknown operation type: {}", agreementBundleDTO.getOperation());
      throw new IllegalArgumentException(
          "Unknown operation type: " + agreementBundleDTO.getOperation());
    }
  }

  void processResponse(ResponseEntity<Void> responseEntity, AgreementBundleDTO agreementBundleDTO) {
    try {
      if (responseEntity.getStatusCode().is2xxSuccessful()) {
        log.info("Response status: {}", responseEntity.getStatusCode());
      } else {
        log.error("Response status: {}", responseEntity.getStatusCode());
        //todo send to dead letter queue
      }
    } catch (Exception e) {
      log.error("Error processing response for agreementNo {}: {}",
          agreementBundleDTO.getAgreementNo(), e.getMessage());
      throw new RuntimeException(
          "Error processing response for agreementNo " + agreementBundleDTO.getAgreementNo(), e);
    }
  }


}
