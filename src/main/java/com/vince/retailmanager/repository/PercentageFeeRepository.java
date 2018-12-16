package com.vince.retailmanager.repository;

import com.vince.retailmanager.entity.PercentageFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PercentageFeeRepository extends JpaRepository<PercentageFee, Integer> {
	List<PercentageFee> findAllByFranchisorId(int id);

}
