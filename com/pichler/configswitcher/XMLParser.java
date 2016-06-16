package com.pichler.configswitcher;

import com.pichler.configswitcher.config.Configurations;
import com.pichler.configswitcher.model.*;
import com.pichler.configswitcher.util.LambdaHelper;
import com.pichler.configswitcher.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by Patrick on 18.05.2016.
 */
public class XMLParser {

  private DocumentBuilder documentBuilder;

  private static class InstanceHolder {
    private static XMLParser INSTANCE = new XMLParser();
  }

  public static XMLParser getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private XMLParser() {
    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public Configurations loadConfigurations(InputStream input) throws IOException, SAXException {
    Document parse = documentBuilder.parse(input);
    NodeList childNodes = parse.getFirstChild().getChildNodes();

    Element tasksElement = getFirstElementImportant(childNodes, "tasks");
    Element configsElement = getFirstElementImportant(childNodes, "configs");
    Element transformersElement = getFirstElementImportant(childNodes, "transformers");

    Collection<Element> taskElements = XMLUtil.getElements(tasksElement, "task");
    List<TaskSupplier> taskSuppliers = taskElements.stream()
        .map(this::parseFromElement)
        .collect(Collectors.toList());
    Map<String, TaskSupplier> taskSupplierMap = taskSuppliers.stream()
        .filter(LambdaHelper.distinct(TaskSupplier::getName))
        .collect(Collectors.toMap(TaskSupplier::getName, Function.identity()));

    Collection<Element> transformerElements = XMLUtil.getElements(transformersElement, "transformer");
    Map<String, Transformer> transformerMap = transformerElements.stream()
        .map(this::parseTransformer)
        .filter(Objects::nonNull)
        .collect(Collectors.toMap(Transformer::getName, Function.identity()));

    Collection<Element> configElements = XMLUtil.getElements(configsElement.getChildNodes(), "config");
    List<Configuration> configs = configElements.stream()
        .map(e -> parseConfiguration(e, taskSupplierMap, transformerMap))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    Map<String, Configuration> configsMap = configs.stream()
        .filter(Objects::nonNull)
        .filter(LambdaHelper.distinct(Configuration::getName))
        .collect(Collectors.toMap(Configuration::getName, Function.identity()));

    return new Configurations(taskSupplierMap, configsMap);
  }

  private Element getFirstElementImportant(NodeList list, String name) {
    Collection<Element> elements = XMLUtil.getElements(list, name);

    if (elements.isEmpty()) {
      throw new IllegalStateException("Config file must have " + name + " defined!");
    }

    return elements.iterator().next();
  }

  public Configuration parseConfiguration(Element element, Map<String, TaskSupplier> taskSupplierMap, Map<String, Transformer> transformerMap) {
    String name = element.getAttribute("name");
    Collection<Element> elements = XMLUtil.getElements(element.getChildNodes());
    List<TaskDraft> taskDrafts = elements.stream()
        .map(e -> getTaskDraft(e, taskSupplierMap, transformerMap))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    return new Configuration(taskDrafts, name);
  }

  public TaskDraft getTaskDraft(Element element, Map<String, TaskSupplier> taskSupplierMap, Map<String, Transformer> transformerMap) {
    String taskName = element.getTagName();
    Map<String, Supplier<String>> parameterMap = getParameterMap(element, transformerMap);

    TaskSupplier taskSupplier = taskSupplierMap.get(taskName);

    if (taskSupplier == null) {
      System.out.println("Task " + taskName + " is not defined!");
      return null;
    }

    return taskSupplier.getTask(parameterMap, XMLUtil.getAttributes(element));
  }

  public Map<String, Supplier<String>> getParameterMap(Element parentElement, Map<String, Transformer> transformerMap) {
    Map<String, Element> map = XMLUtil.getElements(parentElement.getChildNodes()).stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toMap(Element::getTagName, Function.identity()));

    Map<String, Supplier<String>> returnMap = new HashMap<>();

    map.forEach((parameterName, element) -> {
      Map<String, String> attributes = XMLUtil.getAttributes(element);
      String transformerName = attributes.get("transformer");

      if (transformerName == null || transformerName.trim().isEmpty() || !transformerMap.containsKey(transformerName)) {
        returnMap.put(parameterName, element::getTextContent);
        return;
      }

      Transformer transformer = transformerMap.get(transformerName);

      if (Boolean.valueOf(attributes.getOrDefault("immediate", "false"))) {
        String value = transformer.transform(attributes, element);
        returnMap.put(parameterName, () -> value);
      } else {
        returnMap.put(parameterName, () -> transformer.transform(attributes, element));
      }

    });

    return returnMap;
  }

  public TaskSupplier parseFromElement(Element element) {
    String name = element.getAttribute("name");
    String taskClz = element.getAttribute("class");

    if (name == null || taskClz == null) {
      System.out.println("Name or class is null for element: " + element);
      return null;
    }

    try {
      return new TaskSupplier((Class<? extends Task>) Class.forName(taskClz), name, XMLUtil.getAttributes(element));
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    return null;
  }

  public Transformer parseTransformer(Element element) {
    Map<String, String> attributes = XMLUtil.getAttributes(element);
    String name = attributes.get("name");
    String clz = attributes.get("class");

    if (name == null || clz == null) {
      return null;
    }

    try {
      Class<Transformer> targetClass = (Class<Transformer>) Class.forName(clz);

      Transformer transformer = targetClass.newInstance();

      transformer.setName(name);
      transformer.initialize(attributes);

      return transformer;
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }

}
