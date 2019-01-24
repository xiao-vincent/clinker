package com.vince.retailmanager.web;

import com.vince.retailmanager.repository.UserRepository;
import com.vince.retailmanager.service.FinancialService;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.TransactionService;
import com.vince.retailmanager.service.UserService;
import javax.sql.DataSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerUnitTestConfiguration {

  @MockBean
  private DataSource dataSource;

  //mock repositories
  @MockBean
  private UserRepository userRepository;

  //mock services
  @MockBean
  private UserService userService;
  @MockBean
  public TransactionService transactionService;
  @MockBean
  public FinancialService financialService;
  @MockBean
  private FranchiseService franchiseService;
}
