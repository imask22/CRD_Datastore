package com.Dto;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;

//Helper JsonObject---> this is what Json object looks Like 
public class JsonObject implements Serializable {
	
	private String key;
	
	private String value;
	
	public JsonObject()
	{
		
	}
	public JsonObject(String key,String value)
	{
		this.key = key;
		this.value = value;
	}
	
	HashMap<String,String> getObject(String key,String value)
	{
		HashMap<String,String> hmap = new HashMap<String,String>();
		hmap.put(this.key, this.value);
		return hmap;
	}
	//to print response
	public String toString() 
    { 
        return this.key+" "+this.value; 
    } 
	
	
}
