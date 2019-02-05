package com.vince.retailmanager.web;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

final class WebTestUtil {

  private WebTestUtil() {
  }


  public static byte[] convertToBytes(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsBytes(object);
  }

  public static String convertToString(Object object, Class<?>... view) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    for (Class<?> viewClass : view) {
      mapper.setConfig(mapper.getSerializationConfig().withView(viewClass));
    }
    return mapper.writeValueAsString(object);
  }

//  public static String convertToString(Object object) throws IOException {
//    return convertToString(object, null);
//  }

}