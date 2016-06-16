package com.pichler.configswitcher.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ppichler on 13.06.2016.
 */
public class Context {
  private static Context current;

  public static Context getCurrent() {
    if (current == null) {
      return startNewContext();
    }

    return current;
  }

  public static Context startNewContext() {
    current = new Context();
    return current;
  }

  private Map<String, Object> parameters = new HashMap<>();

  private Context() {
  }

  public void putParameter(String key, Object value) {
    parameters.put(key, value);
  }

  public Object getParameter(String key) {
    return parameters.get(key);
  }

  public Object getParameter(String key, Object defaultValue) {
    return parameters.getOrDefault(key, defaultValue);
  }

  public void clear() {
    parameters.clear();
  }
}
