package com.demo.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigProperties {
	
	

	public static String getValueByKey(String key){
		Properties prop =  new  Properties();    
	    InputStream in = ConfigProperties.class.getResourceAsStream( "/config.properties" );
	    try  {    
	        prop.load(in); 
	        return prop.getProperty(key).trim(); 
	        
	    }catch(Exception e){
	    	System.out.println("Load config.properties has an error!");
	    	return "";
	    }
		
	    
		
	}
	
	

	public static String getCNnameByFilename(String filename,String modelType){
		

		//String key = modelType.replace("_", "")+"."+filename.substring(3);
		int index = 1 + filename.lastIndexOf("_") - filename.indexOf("_");
		String key = modelType.replace("_", "")+"."+filename.substring(index);

		Properties prop =  new  Properties();    
//	    InputStream in = ConfigProperties.class.getResourceAsStream( "/filename.properties" );
	    try  {    
	        prop.load(new InputStreamReader(ConfigProperties.class.getResourceAsStream("/filename.properties"), "UTF-8")); 
	        
	        return prop.getProperty(key).trim(); 
	        
	    }catch(Exception e){
	    	System.out.println("Load filename.properties has an error!");
	    	return "";
	    }
		
	    
		
	}
	
	public static void main(String[] args) {
		String cnname = getCNnameByFilename("1_1_charactersummary.txt", "panel");
		System.out.println(cnname);
	}
	

}
