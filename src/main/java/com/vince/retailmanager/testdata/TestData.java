package com.vince.retailmanager.testdata;

import com.vince.retailmanager.model.entity.authorization.User;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.security.RoleType;
import com.vince.retailmanager.service.UserService;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TestData {

  static final String DUMMY_PASSWORD = "password";

  public static User createUser() {
    User user = new User();
    user.setUsername("username");
    user.setPassword(DUMMY_PASSWORD);
    return user;
  }

  public static Franchisor createFranchisor() {
    return Franchisor.builder()
        .name("McDonald's Corporation")
        .website("mcdonalds.com")
        .description("Fast food company")
        .franchiseFee(30000.0)
        .liquidCapitalRequirement(200000.0)
        .royaltyFeePercent(.08)
        .marketingFeePercent(.02)
        .feeFrequency(12)
        .build();
  }

  public static void setupAdmin(UserService userService) throws Exception {
    User admin = new User("admin", DUMMY_PASSWORD);
    admin.addRole(RoleType.ADMIN);
    userService.saveUser(admin);
  }

}