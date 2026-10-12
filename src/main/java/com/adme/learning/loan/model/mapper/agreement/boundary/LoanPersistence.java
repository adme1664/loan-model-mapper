package com.adme.learning.loan.model.mapper.agreement.boundary;

import com.adme.learning.loan.model.mapper.agreement.entity.BaseEntity;
import com.adme.learning.loan.model.mapper.agreement.entity.BaseSource;
import java.util.Optional;

public interface LoanPersistence<E extends BaseEntity, S extends BaseSource> {

  void saveOrUpdate(S source);
  Optional<E> findById(String id);
  void deleteById(String id);
  boolean exists(String id);

}
