package com.vince.retailmanager.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vince.retailmanager.demo.TestData;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(ControllerUnitTestConfiguration.class)
@Category(UnitTest.class)
public class UserControllerTest {

  private HttpRequestBuilder requestBuilder;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserService userService;

  @BeforeEach
  void beforeEach() {
//    this.requestBuilder = new HttpRequestBuilder(this.mockMvc);
  }

  @Nested
  class Create {

    private User input;

    @BeforeEach
    void beforeEach() {
      this.input = TestData.createUser();
      requestBuilder = HttpRequestBuilder.builder()
          .mockMvc(mockMvc)
          .route("/users")
          .build();
    }

    @Nested
    class ValidFields {

      private User returnUserSuccess;

      @BeforeEach
      void beforeEach() {
        returnUserSuccess = TestData.createUser();
        returnUserSuccess.setPassword("");
        given(userService.saveUser(any(User.class))).willReturn(returnUserSuccess);
      }

      @Test
      @WithMockUser(roles = RoleType.Constants.USER)
      void shouldReturnStatusCodeCreated() throws Exception {
        requestBuilder.createUser(input).andExpect(status().isCreated());
      }

      @Test
      @WithMockUser(roles = RoleType.Constants.USER)
      void shouldReturnCreatedUserWithJson() throws Exception {
        requestBuilder.createUser(input)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
      }
    }

    @Test
    void shouldReturnStatusCodeUnauthorized() throws Exception {
      requestBuilder.createUser(this.input)
          .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = RoleType.Constants.USER)
    void shouldReturnStatusCodeBadRequest() throws Exception {
      this.input.setUsername("");
      requestBuilder.createUser(this.input)
          .andExpect(status().isBadRequest());

      this.input.setUsername("a3");
      requestBuilder.createUser(this.input)
          .andExpect(status().isBadRequest());
    }

  }
}