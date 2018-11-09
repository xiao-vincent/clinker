package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Franchisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

@SpringBootTest
@ExtendWith(SpringExtension.class)
//@ExtendWith(MockitoExtension.class)
class FranchiseServiceTest {

	@Autowired
	private FranchiseService franchiseService;

	@BeforeEach
	void setUp() {
	}


	@Test
	public void insertFranchisee() {

		Franchisor franchisor = new Franchisor("Mcdonalds", "www.mcd.com", "fastfood", null);

		this.franchiseService.saveFranchisor(franchisor);

		Collection<Franchisor> franchisors = this.franchiseService.findAllFranchisors();
		System.out.println(franchisors);
	}


}