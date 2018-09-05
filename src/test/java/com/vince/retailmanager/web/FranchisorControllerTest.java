package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.service.BusinessService;
import com.vince.retailmanager.service.businessService.ApplicationTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link FranchisorController}
 */
//@SpringBootTest(classes = {FranchisorController.class, BusinessServiceImpl.class, AuthenticationAdapter.class})
@WebMvcTest(FranchisorController.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTestConfig.class)
public class FranchisorControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private BusinessService businessService;

    private List<Franchisor> franchisors;

    @Before
    public void setUp() throws Exception {
        franchisors = new ArrayList<>();
        Franchisor franchisor = new Franchisor();
        franchisor.setId(1);
        franchisors.add(franchisor);


    }

    @Test
//    @WithMockUser(roles = "USER")
    public void testGetOwnerNotAuthorized() throws Exception {
        given(this.businessService.findFranchisorById(1)).willReturn(franchisors.get(0));
        this.mockMvc.perform(get("/franchisors/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(roles = "USER")
    public void testGetFranchisorSuccess() throws Exception {
        given(this.businessService.findFranchisorById(1)).willReturn(franchisors.get(0));
        this.mockMvc.perform(get("/franchisors/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.firstName").value("George"));
        ;
    }
}