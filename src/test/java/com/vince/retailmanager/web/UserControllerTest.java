package com.vince.retailmanager.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vince.retailmanager.model.entity.authorization.User;
import com.vince.retailmanager.security.RoleType;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.controller.UserController;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/*
 * 1. ok status
 * 2. validation
 * */

@WebMvcTest(UserController.class)
@Import(ControllerUnitTestConfiguration.class)
@Category(UnitTest.class)
public class UserControllerTest {

  private TaskHttpRequestBuilder requestBuilder;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserService userService;

  @BeforeEach
  void beforeEach() {
    this.requestBuilder = new TaskHttpRequestBuilder(this.mockMvc);
  }

  private User createValidUser() {
    User user = new User();
    user.setUsername("username");
    user.setPassword("password123");
    return user;
  }

  @Nested
  public class Create {

    private User user;

    @BeforeEach
    void beforeEach() {
      this.user = UserControllerTest.this.createValidUser();
    }

    @Test
    @WithMockUser(roles = RoleType.Constants.USER)
    void createUserSuccess() throws Exception {
      User returnUser = UserControllerTest.this.createValidUser();
      returnUser.setPassword("");
      given(UserControllerTest.this.userService.saveUser(any(User.class))).willReturn(returnUser);
      UserControllerTest.this.requestBuilder.createUser(this.user).andExpect(status().isCreated());
    }

    @Test
    void createUserUnauthorized() throws Exception {
      UserControllerTest.this.requestBuilder.createUser(this.user)
          .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = RoleType.Constants.USER)
    void usernameError() throws Exception {
      this.user.setUsername("");
      UserControllerTest.this.requestBuilder.createUser(this.user)
          .andExpect(status().isBadRequest());

      this.user.setUsername("a3");
      UserControllerTest.this.requestBuilder.createUser(this.user)
          .andExpect(status().isBadRequest());
    }

  }
}