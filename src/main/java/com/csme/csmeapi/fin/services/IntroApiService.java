package com.csme.csmeapi.fin.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cs.base.xml.XMLManager;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.models.IntroResponse;

/**
 * This class provides services related to introductory information retrieval.
 */
public class IntroApiService {
	
	FinUtil finUtil = new FinUtil();
	/**
     * Retrieves the introductory information based on the request ID and sequence.
     *
     * @param requestId The unique identifier for the request.
     * @param sequence  The sequence number for the request.
     * @return An IntroResponse object containing the introductory information.
     * @throws Exception If an error occurs during information retrieval.
     */
	public IntroResponse getIntroduction(UUID requestId, Long sequence) throws Exception {
		IntroResponse resp = new IntroResponse();
		try {
			String introPath = FinUtil.intPath+"Intro.xml";
			Document introDoc = XMLManager.xmlFileToDom(introPath);
			Element rootElement = introDoc.getDocumentElement();
	        Map<String, List<Map<String, String>>> elementsMap = new HashMap<>();
	        NodeList childNodes = rootElement.getChildNodes();
	        for (int i = 0; i < childNodes.getLength(); i++) {
	            Node node = childNodes.item(i);
	            if (node.getNodeType() == Node.ELEMENT_NODE) {
	                Element element = (Element) node;
	                String nodeName = element.getNodeName();
	                List<Map<String, String>> elementList = elementsMap.computeIfAbsent(nodeName, k -> new ArrayList<>());
	                elementList.add(buildElementMap(element));
	            }
	        }
	        elementsMap.forEach((key, value) -> {
	            if (value.size() == 1) {
	                setProperty(resp, key, value.get(0));
	            } else {
	                setProperty(resp, key, value);
	            }
	        });
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	public static void main(String[] args) throws Exception {
		IntroResponse resp = new IntroResponse();
        String introPath = "C:\\Users\\ilyas\\Desktop\\Ilyas\\LastestFinMobileApp\\src\\main\\resources\\Intro.xml";
        Document introDoc = XMLManager.xmlFileToDom(introPath);
        Element rootElement = introDoc.getDocumentElement();
        Map<String, List<Map<String, String>>> elementsMap = new HashMap<>();
        NodeList childNodes = rootElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String nodeName = element.getNodeName();
                List<Map<String, String>> elementList = elementsMap.computeIfAbsent(nodeName, k -> new ArrayList<>());
                elementList.add(buildElementMap(element));
            }
        }
        elementsMap.forEach((key, value) -> {
            if (value.size() == 1) {
                setProperty(resp, key, value.get(0));
            } else {
                setProperty(resp, key, value);
            }
        });
        System.out.println(resp);
	}
	/**
     * Sets a property in the IntroResponse object based on the provided key and value.
     *
     * @param resp The IntroResponse object to set the property in.
     * @param key  The key of the property.
     * @param value The value of the property.
     */
    private static void setProperty(IntroResponse resp, String key, Object value) {
        switch (key) {
            case "VersionInfo":
                resp.setVersionInfo(value);
                break;
            case "LandingPage":
                resp.setLandingPage(value);
                break;
            case "LoginPage":
                resp.setLoginPageInfo(value);
                break;   
            case "PageInfo":
                resp.setPageInfo(value);
                break;
            case "disclaimer":
                resp.setDisclaimer(value);
                break;
            default:
                break;
        }
    }
    /**
     * Builds a map of element names and their corresponding text content from an XML element.
     *
     * @param element The XML element to build the map from.
     * @return A map containing element names and their text content.
     */
    private static Map<String, String> buildElementMap(Element element) {
        Map<String, String> elementMap = new HashMap<>();
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                elementMap.put(childElement.getNodeName(), childElement.getTextContent());
            }
        }
        return elementMap;
    }
    
}