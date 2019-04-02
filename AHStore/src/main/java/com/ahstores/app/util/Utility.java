package com.ahstores.app.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class Utility {

	public static  ObjectMapper mapperObj = new ObjectMapper();
	
	public static <T> T getObjectFromJson(String json, Class<T> clazz) {
		
		T obj = null;
		
		try {
			obj = mapperObj.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public static String getJsonFromObject(Object object) {
		
		String json = "";
		try {
			json = mapperObj.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
}
