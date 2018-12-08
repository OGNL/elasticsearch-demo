package com.demo.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
	
	

	public static String getValueByKey(String key){
		Properties prop =  new  Properties();    
	    InputStream in = ConfigUtil.class.getResourceAsStream( "/config.properties" );
	    try  {    
	        prop.load(in); 
	        return prop.getProperty(key).trim(); 
	        
	    }catch(Exception e){
	    	System.out.println("Load config.properties has an error!");
	    	return "";
	    }

	}

	

}
