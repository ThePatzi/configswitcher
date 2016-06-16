package com.pichler.configswitcher.transformers;

import com.pichler.configswitcher.config.Application;
import com.pichler.configswitcher.config.Context;
import org.w3c.dom.Element;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ppichler on 16.06.2016.
 */
public class PathBuilderTransformer extends AbstractTransformer {
  @Override
  public String transform(Map<String, String> parameters, Element element) {
    String parentAttrName = parameters.get("parentPathAttrName");

    if (parentAttrName == null || parentAttrName.trim().isEmpty()) {
      throw new RuntimeException("No attribute specified!");
    }

    boolean context = Boolean.valueOf(parameters.getOrDefault("useContext", "false"));

    String parentDir = context ? Context.getCurrent().getParameter(parentAttrName) + "" :
        Application.getInstance().getParameter(parentAttrName);

    if (parentDir == null || parentDir.trim().isEmpty()) {
      throw new RuntimeException("No application parameter with the name '" + parentAttrName + "'");
    }

    String paths = element.getTextContent().trim();

    return Arrays.stream(paths.split(";"))
        .map(str -> {
          if (!str.startsWith("^")) {
            return Paths.get(parentDir, str) + "";
          } else {
            return Paths.get(str) + "";
          }
        })
        .collect(Collectors.joining(";"));
  }
}
