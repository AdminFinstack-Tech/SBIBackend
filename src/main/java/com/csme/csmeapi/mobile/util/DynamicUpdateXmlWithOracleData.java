package com.csme.csmeapi.mobile.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DynamicUpdateXmlWithOracleData {
	
	private static final String USER_NAME = "CETRX";
	private static final String DB_URL = "jdbc:oracle:thin:@DEV-ARB:1521:DARB";
	private static final String DATABASE_DRIVER = "oracle.jdbc.driver.OracleDriver";

	private static Connection getDatabaseConnection()	throws Exception {
		Connection conn;
		Class.forName(DATABASE_DRIVER);
		conn=DriverManager.getConnection(DB_URL,USER_NAME,USER_NAME);
		return conn;
	}

    public static void main(String[] args) throws Exception {
        try {
            Connection connection = getDatabaseConnection();
            String sqlQuery = "SELECT C_MAIN_REF, GTEE_TYPE, APPL_NAME, APPL_ADD FROM EXIMTRX.GTEE_MASTER WHERE 1=1 AND C_MAIN_REF = ? AND I_EVENT_TIMES = ?";
            String mainRefParam = "19OGTE48800655";
            String eventTimesParam = "1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            	preparedStatement.setString(1, mainRefParam);
                preparedStatement.setString(2, eventTimesParam);
                String generatedSql = preparedStatement.toString();
                System.out.println("Generated SQL query: " + generatedSql);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse("C:\\Users\\ilyas\\Desktop\\text.xml");
                    Element rootElement = document.getDocumentElement();
                    NodeList lgInfoList = rootElement.getElementsByTagName("LGInfo");
                    if (lgInfoList.getLength() > 0) {
                        Element lgInfoElement = (Element) lgInfoList.item(0);
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        while (resultSet.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                System.out.println("columnName is :"+columnName);
                                String columnValue = resultSet.getString(i);
                                System.out.println("columnValue is :"+columnValue);
                                String nodeName = getNodeNameByValue(document, columnName);
                                NodeList nodeList = rootElement.getElementsByTagName(nodeName);
                                for (int j = 0; j < nodeList.getLength(); j++) {
                                    Element xmlElement = (Element) nodeList.item(j);
                                    xmlElement.setTextContent(columnValue);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String getNodeNameByValue(Document document, String value) {
        NodeList allNodes = document.getElementsByTagName("*");
        for (int i = 0; i < allNodes.getLength(); i++) {
            Node node = allNodes.item(i);
            if (node.getTextContent().equals(value)) {
                return node.getNodeName();
            }
        }
        return null;
    }
}
