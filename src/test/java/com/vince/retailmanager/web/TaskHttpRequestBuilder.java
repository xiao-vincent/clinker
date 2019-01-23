package com.vince.retailmanager.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

public final class TaskHttpRequestBuilder {

  private final MockMvc mockMvc;

  public TaskHttpRequestBuilder(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  public ResultActions createUser(Object input) throws Exception {
    return mockMvc.perform(post("/users")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .content(WebTestUtil.convertObjectToJsonBytes(input))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .with(csrf()));
  }

}
