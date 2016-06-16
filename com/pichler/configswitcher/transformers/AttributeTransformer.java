package com.pichler.configswitcher.transformers;

import com.pichler.configswitcher.config.Application;
import com.pichler.configswitcher.config.Context;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * Created by ppichler on 16.06.2016.
 */
public class AttributeTransformer extends AbstractTransformer {
  @Override
  public String transform(Map<String, String> parameters, Element element) {
    String attrName = parameters.get("attr");
    String defaultValue = parameters.get("default");
    boolean useContext = Boolean.valueOf(parameters.getOrDefault("useContext", "false"));

    String value = useContext ? Context.getCurrent().getParameter(attrName) + "" : Application.getInstance().getParameter(attrName);

    return value == null ? defaultValue : value;
  }
}
