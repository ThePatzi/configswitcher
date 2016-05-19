package com.pichler.configswitcher;

import com.pichler.configswitcher.config.Configurations;
import com.pichler.configswitcher.model.Configuration;
import com.pichler.configswitcher.model.Task;
import com.pichler.configswitcher.model.TaskDraft;
import com.pichler.configswitcher.model.TaskSupplier;
import com.pichler.configswitcher.util.LambdaHelper;
import com.pichler.configswitcher.util.XMLUtil;
import com.sun.javaws.jnl.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
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
        Collection<Element> tasksElements = XMLUtil.getElements(parse.getFirstChild().getChildNodes(), "tasks");

        if (tasksElements.isEmpty()) {
            throw new IllegalStateException("Config file must have tasks defined!");
        }

        Element tasksElement = tasksElements.iterator().next();

        Collection<Element> configsElements = XMLUtil.getElements(parse.getFirstChild().getChildNodes(), "configs");

        if (configsElements.isEmpty()) {
            throw new IllegalStateException("Config file must have configs defined!");
        }

        Element configsElement = configsElements.iterator().next();

        Collection<Element> taskElements = XMLUtil.getElements(tasksElement.getChildNodes(), "task");
        List<TaskSupplier> taskSuppliers = taskElements.stream()
                .map(this::parseFromElement)
                .collect(Collectors.toList());
        Map<String, TaskSupplier> taskSupplierMap = taskSuppliers.stream()
                .filter(LambdaHelper.distinct(TaskSupplier::getName))
                .collect(Collectors.toMap(TaskSupplier::getName, Function.identity()));

        Collection<Element> configElements = XMLUtil.getElements(configsElement.getChildNodes(), "config");
        List<Configuration> configs = configElements.stream()
                .map(e -> parseConfiguration(e, taskSupplierMap))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<String, Configuration> configsMap = configs.stream()
                .filter(Objects::nonNull)
                .filter(LambdaHelper.distinct(Configuration::getName))
                .collect(Collectors.toMap(Configuration::getName, Function.identity()));

        return new Configurations(taskSupplierMap, configsMap);
    }

    public Configuration parseConfiguration(Element element, Map<String, TaskSupplier> taskSupplierMap) {
        String name = element.getAttribute("name");
        Collection<Element> elements = XMLUtil.getElements(element.getChildNodes());
        List<TaskDraft> taskDrafts = elements.stream()
                .map(e -> getTaskDraft(e, taskSupplierMap))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new Configuration(taskDrafts, name);
    }

    public TaskDraft getTaskDraft(Element element, Map<String, TaskSupplier> taskSupplierMap) {
        String taskName = element.getTagName();
        Map<String, String> parameterMap = getParameterMap(element);

        TaskSupplier taskSupplier = taskSupplierMap.get(taskName);

        if (taskSupplier == null) {
            System.out.println("Task " + taskName + " is not defined!");
            return null;
        }

        return taskSupplier.getTask(parameterMap, XMLUtil.getAttributes(element));
    }

    public Map<String, String> getParameterMap(Element element) {
        return XMLUtil.getElements(element.getChildNodes()).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Element::getTagName, Element::getTextContent));
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

}
