package com.adme.learning.loan.model.mapper.creditfacility.boundary;

import com.adme.learning.loan.model.mapper.common.BundleContext;
import com.adme.learning.loan.model.mapper.creditfacility.control.AgreementRelationMapper;
import com.adme.learning.loan.model.mapper.creditfacility.control.CfPartyMapper;
import com.adme.learning.loan.model.mapper.creditfacility.control.CreditFacilityMapper;
import com.adme.learning.loan.model.mapper.creditfacility.control.DealMapper;
import com.adme.learning.loan.model.mapper.creditfacility.entity.DealSource;
import com.adme.learning.model.generator.AgreementRelationDTO;
import com.adme.learning.model.generator.CfPartyDTO;
import com.adme.learning.model.generator.CreditFacilityDTO;
import com.adme.learning.model.generator.DealBundleDTO;
import com.adme.learning.model.generator.DealDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
@Slf4j
public class ProcessingCreditFacility {

  private final DealMapper dealMapper;
  private final CreditFacilityMapper creditFacilityMapper;
  private final AgreementRelationMapper agreementRelationMapper;
  private final CfPartyMapper cfPartyMapper;
  private final CreditFacilityBundleResourceClient bundleApiClient;

  public ProcessingCreditFacility(DealMapper dealMapper, CreditFacilityMapper creditFacilityMapper,
      AgreementRelationMapper agreementRelationMapper, CfPartyMapper cfPartyMapper,
      @Value("${agreement.api.base-url}") String agreementApiBaseUrl) {
    this.dealMapper = dealMapper;
    this.creditFacilityMapper = creditFacilityMapper;
    this.agreementRelationMapper = agreementRelationMapper;
    this.cfPartyMapper = cfPartyMapper;

    WebClient agreementWebClient = WebClient.builder().baseUrl(agreementApiBaseUrl).build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(agreementWebClient))
        .build();
    this.bundleApiClient = factory.createClient(CreditFacilityBundleResourceClient.class);
  }


  public DealBundleDTO prepareCreditFacilityBundle(DealSource dealSource, BundleContext context) {
    DealBundleDTO dealBundleDTO = new DealBundleDTO();

    try {
      DealDTO dealDTO = dealMapper.mapTo(dealSource, context);
      dealBundleDTO.setDeal(dealDTO);
      dealBundleDTO.setDealNo(dealSource.getId());
      dealBundleDTO.setOperation(context.eventName());

      //set credit facility
      List<CreditFacilityDTO> creditFacilityDTOs = getCreditFacilityDTOS(
          dealSource);
      dealBundleDTO.setCreditFacilities(creditFacilityDTOs);

      //set party
      CfPartyDTO partyDTO = cfPartyMapper.mapTo(dealSource.getParties().getFirst());
      dealBundleDTO.setParties(List.of(partyDTO));

      //
    } catch (Exception e) {
      log.error("Error preparing credit facility bundle: {}", e.getMessage());
      throw e;
    }
    return dealBundleDTO;

  }

  private @NonNull List<CreditFacilityDTO> getCreditFacilityDTOS(DealSource dealSource) {
    List<CreditFacilityDTO> creditFacilityDTOs = new ArrayList<>();
    if (dealSource.getCreditFacility() != null) {
      var creditFacilities = dealSource.getCreditFacility();
      creditFacilities.forEach(cf -> {
        CreditFacilityDTO dto = creditFacilityMapper.mapTo(cf);
        dto.setAgreementRelations(new ArrayList<>());

        //set agreement relation
        cf.getAgreements().forEach(agreement -> {
          AgreementRelationDTO agreementRelationDTO = agreementRelationMapper.mapTo(agreement);
          dto.getAgreementRelations().add(agreementRelationDTO);
        });
        creditFacilityDTOs.add(dto);
      });
    }
    return creditFacilityDTOs;
  }

  public void processingCreditFacility(DealSource dealSource, BundleContext context) {
    DealBundleDTO dealBundleDTO = prepareCreditFacilityBundle(dealSource, context);

    if (Objects.nonNull(dealBundleDTO)) {
      synchronize(dealBundleDTO);
    }
  }

  void synchronize(DealBundleDTO dealBundleDTO) {
    ResponseEntity<Void> response = switch (dealBundleDTO.getOperation()) {
      case "INSERT" -> this.bundleApiClient.createAgreementBundle(dealBundleDTO);
      case "MODIFY" -> this.bundleApiClient.updateAgreementBundle(dealBundleDTO);
      case "REMOVE" -> this.bundleApiClient.purgeAgreementBundle(dealBundleDTO);
      default -> {
        log.warn("Unknown operation type: {}", dealBundleDTO.getOperation());
        throw new IllegalArgumentException(
            "Unknown operation type: " + dealBundleDTO.getOperation());
      }
    };

    if (Objects.nonNull(response)) {
      processResponse(response, dealBundleDTO);
    }
  }

  void processResponse(ResponseEntity<Void> responseEntity, DealBundleDTO dealBundleDTO) {
    try {
      if (responseEntity.getStatusCode().is2xxSuccessful()) {
        log.info("Response status: {}", responseEntity.getStatusCode());
      } else {
        log.error("Response status: {}", responseEntity.getStatusCode());
        //todo send to dead letter queue
      }
    } catch (Exception e) {
      log.error("Error processing response for agreementNo {}: {}",
          dealBundleDTO.getDealNo(), e.getMessage());
      throw new RuntimeException(
          "Error processing response for agreementNo " + dealBundleDTO.getDealNo(), e);
    }
  }


}
