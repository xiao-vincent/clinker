package com.vince.retailmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
  }


}