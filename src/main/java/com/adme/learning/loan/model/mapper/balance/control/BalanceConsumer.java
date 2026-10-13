package com.adme.learning.loan.model.mapper.balance.control;

import com.adme.learning.loan.model.mapper.agreement.control.BalanceMapper;
import com.adme.learning.loan.model.mapper.agreement.entity.Balance;
import com.adme.learning.loan.model.mapper.agreement.entity.BalanceSource;
import com.adme.learning.loan.model.mapper.balance.boundary.ProcessingBalance;
import com.adme.learning.loan.model.mapper.common.BundleContext;
import com.adme.learning.loan.model.mapper.common.Event;
import com.adme.learning.loan.model.mapper.common.ParsingMessage;
import com.adme.learning.model.generator.BalanceBundleDTO;
import com.adme.learning.model.generator.BalanceDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BalanceConsumer {

  private final ParsingMessage parsingMessage;
  private final BalanceMapper balanceMapper;
  private final ProcessingBalance processingBalance;
  private final Gson gson ;

  public BalanceConsumer(ParsingMessage parsingMessage, BalanceMapper balanceMapper,
      ProcessingBalance processingBalance) {
    this.parsingMessage = parsingMessage;
    this.balanceMapper = balanceMapper;
    this.processingBalance = processingBalance;
    this.gson = new GsonBuilder().serializeNulls().create();
  }

  @KafkaListener(id = "balance-group", topics = "3857.dev.nrt.balances.info")
  public void consumeBalance(List<Event> events) {

      try {

        if (!events.isEmpty()) {

          List<BalanceSource> balanceSources = new ArrayList<>();

          events.forEach(event -> {
            BalanceSource balanceSource = this.gson.fromJson(event.getPayload(),BalanceSource.class);
            balanceSources.add(balanceSource);
          });

          balanceSources.forEach(balance -> {
            List<BalanceDTO> balanceDTOS = balanceMapper.mapToList(balance,
                new BundleContext("", "nrt",
                    Instant.now().toString()));

            BalanceBundleDTO balanceBundleDTO = new BalanceBundleDTO();
            balanceBundleDTO.setAgreementNo(balance.getAgmtId());
            balanceBundleDTO.setBalances(balanceDTOS);
            processingBalance.processBalance(balanceBundleDTO);
          });
        }

      } catch (Exception e) {
        log.error("Error parsing message: {}", e.getMessage());
      }
    }

}
