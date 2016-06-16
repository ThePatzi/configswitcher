package com.pichler.configswitcher.model;

import org.w3c.dom.Element;

import java.util.Map;

/**
 * Created by ppichler on 16.06.2016.
 */
public interface Transformer extends Nameable{

  void initialize(Map<String, String> settings);

  String transform(Map<String, String> parameters, Element element);

}
