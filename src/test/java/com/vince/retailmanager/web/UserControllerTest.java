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

      private User returnUser;

      @BeforeEach
      void beforeEach() {
        returnUser = TestData.createUser();
        returnUser.setPassword("");
        given(userService.saveUser(any(User.class))).willReturn(returnUser);
      }

      @Test
      @WithMockUser(roles = RoleType.Constants.USER)
      void shouldReturnStatusCodeCreated() throws Exception {
        requestBuilder.makeRequest(input).andExpect(status().isCreated());
      }

      @Test
      @WithMockUser(roles = RoleType.Constants.USER)
      void shouldReturnCreatedUserWithJson() throws Exception {
        requestBuilder.makeRequest(input)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
      }

      @Test
      @WithMockUser(roles = RoleType.Constants.USER)
      void shouldReturnUserInResponseBody() throws Exception {
        requestBuilder.makeRequest(input)
            .andExpect(content()
                .string(WebTestUtil.convertToString(returnUser)));
      }
    }

    @Test
    void shouldReturnStatusCodeUnauthorized() throws Exception {
      requestBuilder.makeRequest(this.input)
          .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = RoleType.Constants.USER)
    void shouldReturnStatusCodeBadRequest() throws Exception {
      this.input.setUsername("");
      requestBuilder.makeRequest(this.input)
          .andExpect(status().isBadRequest());

      this.input.setUsername("a3");
      requestBuilder.makeRequest(this.input)
          .andExpect(status().isBadRequest());
    }

  }
}