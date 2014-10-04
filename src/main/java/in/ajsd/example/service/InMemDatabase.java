package in.ajsd.example.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
public class InMemDatabase {

  private static final Map<String, Object> database = new HashMap<>();

  public InMemDatabase set(String key, Object value) {
    database.put(key, value);
    return this;
  }

  @Nullable
  public Object get(String key) {
    return database.get(key);
  }
}
