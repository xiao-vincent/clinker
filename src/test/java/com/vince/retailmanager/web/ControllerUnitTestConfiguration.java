package com.vince.retailmanager.web;

import com.vince.retailmanager.repository.UserRepository;
import com.vince.retailmanager.service.UserService;
import javax.sql.DataSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerUnitTestConfiguration {

  @MockBean
  private UserService userService;
  @MockBean
  private DataSource dataSource;
  @MockBean
  private UserRepository userRepository;
}
