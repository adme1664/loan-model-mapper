package com.adme.learning.loan.model.mapper.agreement.entity;


import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

  @Id
  String id;
  String eventName;
  Instant createdAt;
  Instant updatedAt;
}
