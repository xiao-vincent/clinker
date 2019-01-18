package com.vince.retailmanager.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.businessService.ApplicationTestConfig;
import com.vince.retailmanager.web.controller.FranchisorController;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link FranchisorController}
 */
//@SpringBootTest(classes = {FranchisorController.class, BusinessServiceImpl.class, AuthenticationAdapter.class})
@WebMvcTest(FranchisorController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationTestConfig.class)
public class FranchisorControllerTest {

  @Autowired
  private MockMvc mockMvc;


  @MockBean
  private FranchiseService franchiseService;

  private List<Franchisor> franchisors;

  @BeforeEach
  public void setUp() throws Exception {
    franchisors = new ArrayList<>();
    Franchisor franchisor = new Franchisor();
    franchisor.setId(1);
    franchisors.add(franchisor);


  }

  @Test
//    @WithMockUser(roles = "USER")
  public void testGetOwnerNotAuthorized() throws Exception {
//		when(this.franchiseService.findFranchisorById(1)).thenReturn(franchisors.get(0));
    this.mockMvc.perform(get("/franchisors/1")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  @WithMockUser(roles = "USER")
  public void testGetFranchisorSuccess() throws Exception {
    when(this.franchiseService.findFranchisorById(1)).thenReturn(franchisors.get(0));
    this.mockMvc.perform(get("/franchisors/1")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.firstName").value("George"));
    ;
  }
}