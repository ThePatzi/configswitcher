package com.pichler.configswitcher.transformers;

import com.pichler.configswitcher.util.XMLUtil;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * Created by ppichler on 16.06.2016.
 */
public class TestTransformer extends AbstractTransformer {
  @Override
  public String transform(Map<String, String> parameters, Element element) {
    System.out.println("transformer says hi");
    System.out.println(parameters);
    return element.getTextContent() + " swag " + XMLUtil.hasChildElements(element);
  }
}
