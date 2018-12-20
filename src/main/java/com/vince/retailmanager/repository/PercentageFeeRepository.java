package com.vince.retailmanager.repository;

import com.vince.retailmanager.model.entity.PercentageFee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PercentageFeeRepository extends JpaRepository<PercentageFee, Integer> {

  List<PercentageFee> findAllByFranchisorId(int id);

}
