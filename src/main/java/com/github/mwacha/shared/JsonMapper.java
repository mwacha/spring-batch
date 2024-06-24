package com.github.mwacha.shared;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class JsonMapper {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
  }

  @SneakyThrows
  public static <T> T toObject(String content, Class<T> valueType) {
    return MAPPER.readValue(content, valueType);
  }

  @SneakyThrows
  public static <T> List<T> toList(String content, Class<T> clazz) {
    return MAPPER.readValue(
        content, TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
  }

  @SneakyThrows
  public static String toJson(Object content) {
    return MAPPER.writeValueAsString(content);
  }
}
