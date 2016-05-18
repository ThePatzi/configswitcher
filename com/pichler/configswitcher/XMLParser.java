package com.pichler.configswitcher;

import com.pichler.configswitcher.config.Configurations;
import com.pichler.configswitcher.model.Task;
import com.pichler.configswitcher.model.TaskSupplier;
import com.pichler.configswitcher.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Patrick on 18.05.2016.
 */
public class XMLParser {

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    private static class InstanceHolder {
        private static XMLParser INSTANCE = new XMLParser();
    }

    public static XMLParser getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private XMLParser() {
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Configurations loadConfigurations(File inputFile) throws IOException, SAXException {
        Document parse = documentBuilder.parse(inputFile);
        Collection<Element> tasksElements = XMLUtil.getElements(parse.getFirstChild().getChildNodes(), "tasks");

        if (tasksElements.isEmpty()) {
            throw new IllegalStateException("Config file must have tasks defined!");
        }

        Element tasksElement = tasksElements.iterator().next();
        Collection<Element> taskElements = XMLUtil.getElements(tasksElement.getChildNodes(), "task");

        return null;
    }

    public TaskSupplier parseFromElement(Element element) throws ClassNotFoundException {
        String name = element.getAttribute("name");
        String taskClz = element.getAttribute("class");

        if (name == null || taskClz == null) {
            System.out.println("Name or class is null for element: " + element);
            return null;
        }

        return new TaskSupplier(name, (Class<? extends Task>) Class.forName(taskClz));
    }
}
