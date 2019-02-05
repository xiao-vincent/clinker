package com.vince.retailmanager.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import lombok.Builder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@Builder
public final class HttpRequestBuilder {

  private final MockMvc mockMvc;
  private final String route;


  public ResultActions makeRequest(Object input) throws Exception {
    return getResultActions(input);
  }

  private ResultActions getResultActions(Object input) throws Exception {
    return mockMvc.perform(post(route)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .content(WebTestUtil.convertToBytes(input))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .with(csrf()))
        .andDo(MockMvcResultHandlers.print());
  }

}
