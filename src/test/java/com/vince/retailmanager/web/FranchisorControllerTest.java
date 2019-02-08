package com.vince.retailmanager.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.testdata.TestData;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.security.RoleType;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.web.controller.FranchisorController;
import com.vince.retailmanager.web.json.View;
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

  @Nested
  class Create {

    @JsonView(View.Franchisor.class)
    private Franchisor input;

    @BeforeEach
    void beforeEach() {
      this.input = TestData.createFranchisor();
      requestBuilder = HttpRequestBuilder.builder()
          .mockMvc(mockMvc)
          .route("/franchisors/new")
          .build();
    }

    @Nested
    class ValidFields {

      @BeforeEach
      void beforeEach() {
        given(franchiseService.saveCompany(any(Franchisor.class))).willReturn(input);
      }

      @Test
      @WithMockUser
      void shouldReturnStatusCodeCreated() throws Exception {
        requestBuilder.makeRequest(input).andExpect(status().isCreated());
      }

      @Test
      @WithMockUser
      void shouldReturnFranchisorInResponseBody() throws Exception {
        requestBuilder.makeRequest(input)
            .andExpect(content().string(WebTestUtil.convertToString(input, View.Franchisor.class)));
      }
    }

    @Test
    void shouldReturnStatusCodeUnauthorized() throws Exception {
//      requestBuilder.makeRequest(this.input)
//          .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = RoleType.Constants.USER)
    void shouldReturnStatusCodeBadRequest() throws Exception {
//      this.input.setUsername("");
//      requestBuilder.makeRequest(this.input)
//          .andExpect(status().isBadRequest());
//
//      this.input.setUsername("a3");
//      requestBuilder.makeRequest(this.input)
//          .andExpect(status().isBadRequest());
    }

  }


}