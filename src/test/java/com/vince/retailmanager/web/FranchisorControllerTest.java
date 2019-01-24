package com.vince.retailmanager.web;

import com.vince.retailmanager.demo.TestData;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.security.RoleType;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.web.controller.FranchisorController;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FranchisorController.class)
@Import(ControllerUnitTestConfiguration.class)
@Category(UnitTest.class)
public class FranchisorControllerTest {

  private HttpRequestBuilder requestBuilder;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private FranchiseService franchiseService;


  @BeforeEach
  public void beforeEach() {
//    this.requestBuilder = new HttpRequestBuilder(this.mockMvc);
  }

  @Nested
  class Create {

    private Franchisor input;

    @BeforeEach
    void beforeEach() {
      this.input = TestData.createFranchisor();
    }

    @Nested
    class ValidFields {

      private Franchisor returnFranchisor;

      @BeforeEach
      void beforeEach() {
//        given(userService.saveUser(any(User.class))).willReturn(returnFranchisor);
      }

      @Test
      @WithMockUser(roles = RoleType.Constants.USER)
      void shouldReturnStatusCodeCreated() throws Exception {
//        requestBuilder.createUser(input).andExpect(status().isCreated());
      }

      @Test
      @WithMockUser(roles = RoleType.Constants.USER)
      void shouldReturnCreatedUserWithJson() throws Exception {
//        requestBuilder.createUser(input)
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
      }
    }

    @Test
    void shouldReturnStatusCodeUnauthorized() throws Exception {
//      requestBuilder.createUser(this.input)
//          .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = RoleType.Constants.USER)
    void shouldReturnStatusCodeBadRequest() throws Exception {
//      this.input.setUsername("");
//      requestBuilder.createUser(this.input)
//          .andExpect(status().isBadRequest());
//
//      this.input.setUsername("a3");
//      requestBuilder.createUser(this.input)
//          .andExpect(status().isBadRequest());
    }

  }


}