package com.csme.csmeapi.fin.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.csme.csmeapi.fin.config.FinUtil;
import com.google.gson.JsonObject;

public class FCMHelper {
	FinUtil finUtil = new FinUtil();
	
	Logger logger = LogManager.getLogger("CSMEMobile");
    private static FCMHelper instance = null;
    private static final String URL_SEND = "https://fcm.googleapis.com/fcm/send";
    public static final String TYPE_TO = "to";  // Use for single devices, device groups and topics
    public static final String TYPE_CONDITION = "condition"; // Use for Conditions
    private static final String FCM_SERVER_KEY = "AAAAbr0zVNw:APA91bEA31ntK-nASSEcmrTdohaXARuSUgrsf9XW_zPj3vmVhtXk_pbVFd2i7yWncczuBs9UEULRREBNfFXP9YZQ3mJzCABzlWO-AOHEifsop4iFyFTTT798QLkYm7zxuvcQpbzrizDc";

    public static FCMHelper getInstance() {
        if (instance == null) instance = new FCMHelper();
        return instance;
    }

    private FCMHelper() {}

   
    public String sendNotification(String type, String typeParameter, JsonObject notificationObject) throws IOException {
        return sendNotifictaionAndData(type, typeParameter, notificationObject, null);
    }

    public String sendData(String type, String typeParameter, JsonObject dataObject) throws IOException {
        return sendNotifictaionAndData(type, typeParameter, null, dataObject);
    }

    public String sendNotifictaionAndData(String type, String typeParameter, JsonObject notificationObject, JsonObject dataObject) throws IOException {
        String result = null;
        if (type.equals(TYPE_TO) || type.equals(TYPE_CONDITION)) {
            JsonObject sendObject = new JsonObject();
            sendObject.addProperty(type, typeParameter);
            result = sendFcmMessage(sendObject, notificationObject, dataObject);
        }
        return result;
    }

    public String sendTopicData(String topic, JsonObject dataObject) throws IOException{
        return sendData(TYPE_TO, "/topics/" + topic, dataObject);
    }

    public String sendTopicNotification(String topic, JsonObject notificationObject) throws IOException{
        return sendNotification(TYPE_TO, "/topics/" + topic, notificationObject);
    }

    public String sendTopicNotificationAndData(String topic, JsonObject notificationObject, JsonObject dataObject) throws IOException{
        return sendNotifictaionAndData(TYPE_TO, "/topics/" + topic, notificationObject, dataObject);
    }

    private String sendFcmMessage(JsonObject sendObject, JsonObject notificationObject, JsonObject dataObject) throws IOException {
        String response = null;
		try {
			HttpPost httpPost = new HttpPost(URL_SEND);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Authorization", "key=" + FCM_SERVER_KEY);
			if (notificationObject != null) sendObject.add("notification", notificationObject);
			if (dataObject != null) sendObject.add("data", dataObject);
			String data = sendObject.toString();
			StringEntity entity = new StringEntity(data);
			httpPost.setEntity(entity);
			HttpClient httpClient = HttpClientBuilder.create().build();
			BasicResponseHandler responseHandler = new BasicResponseHandler();
			response = (String) httpClient.execute(httpPost, responseHandler);
			logger.info("FCM request sent: " + data);  // Log the request sent to FCM
			logger.info("FCM response received: " + response);  // Log the response from FCM
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return response;
    }
    
    public static void main(String[] args) {
        FCMHelper fcmHelper = FCMHelper.getInstance();
        // Create a JsonObject for the notification content
        JsonObject notificationObject = new JsonObject();
        notificationObject.addProperty("title", "Release Notification");
        notificationObject.addProperty("body", "Transaction has waiting in queue 213123 for release.");
        notificationObject.addProperty("badge", "5");
        try {
            String result = fcmHelper.sendNotification(FCMHelper.TYPE_TO, "eilTp-SIHkyEnvCcgHVLCb:APA91bGBQlCENKfyMNnS0P6C2FJaEybTjpb0C6OGBelrEAl09RXefp1ioCS17XlfbLTWQHIPGenC0me4zYmbeZP8gOCwNVDx9opeepnaoctGXvHfSL05J0tQAifc1hN_1NCfR7MvEagB", notificationObject);
            System.out.println("FCM response: " + result);
        } catch (Exception e) {
            System.err.println("Error sending FCM notification: " + e.getMessage());
        }
    }

}