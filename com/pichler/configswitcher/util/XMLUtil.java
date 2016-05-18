package com.pichler.configswitcher.util;

import com.sun.webkit.dom.NodeListImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Patrick on 18.05.2016.
 */
public class XMLUtil {
    public static Collection<Node> getNodes(NodeList nodeList, String elementNames) {
        return nodeStream(nodeList)
                .filter(n -> n.getNodeName().equals(elementNames))
                .collect(Collectors.toList());
    }

    public static Collection<Element> getElements(NodeList nodeList, String elementName) {
        return nodeStream(nodeList)
                .filter(node -> node instanceof Element)
                .map(node -> (Element) node)
                .filter(element -> element.getNodeName().equals(elementName))
                .collect(Collectors.toList());
    }

    public static Collection<Element> getElements(NodeList nodeList){
        return nodeStream(nodeList)
                .filter(node -> node instanceof Element)
                .map(node -> (Element) node)
                .collect(Collectors.toList());
    }

    public static Stream<Node> nodeStream(NodeList nodeList) {
        return IntStream.range(0, nodeList.getLength())
                .mapToObj(nodeList::item);
    }
}
