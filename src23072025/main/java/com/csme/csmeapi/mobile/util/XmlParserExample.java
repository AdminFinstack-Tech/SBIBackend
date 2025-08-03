package com.csme.csmeapi.mobile.util;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlParserExample {
	
	public static String replacePlaceholders(String templateString, Map<String, Object> mapBody) {
        // Flatten nested map if not null, or initialize an empty map
        Map<String, Object> flatMap = mapBody != null ? flattenMap(mapBody, null) : new HashMap<>();

        // Regex pattern to find placeholders in the format {key}
        Pattern placeholderPattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = placeholderPattern.matcher(templateString);

        // Use StringBuilder to create the final result
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            // Replace with the value from the map or "null" if the key is missing
            String replacement = flatMap.containsKey(key) 
                    ? (flatMap.get(key) != null ? flatMap.get(key).toString() : "null") 
                    : "null";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    // Flatten a nested map (recursive helper function)
    private static Map<String, Object> flattenMap(Map<String, Object> map, String parentKey) {
        Map<String, Object> flatMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = parentKey != null ? parentKey + "." + entry.getKey() : entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                // Recursive call for nested map
                flatMap.putAll(flattenMap((Map<String, Object>) value, key));
            } else {
                flatMap.put(key, value);
            }
        }
        return flatMap;
    }

    public static void main(String[] args) {
	        String template = "Hello, {name}. Your order {orderId} is {status}.";
	        Map<String, Object> mapBody = new HashMap<>();
	        mapBody.put("name", "John");
	        mapBody.put("orderId", 12345);

	        // Example usage
	        String result = replacePlaceholders(template, mapBody);
	        System.out.println(result);
	        // Output: Hello, John. Your order 12345 is null.
	    }
    
    // Helper method to get the node name based on a given value
    private static String getNodeNameByValue(Document document, String value) {
        NodeList allNodes = document.getElementsByTagName("*");
        for (int i = 0; i < allNodes.getLength(); i++) {
            Node node = allNodes.item(i);
            if (node.getTextContent().equals(value)) {
                return node.getNodeName();
            }
        }
        return "Not Found";
    }
}
