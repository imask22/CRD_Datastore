package com.Dto;

import java.io.Serializable;

//Dto Class to Initialize
public class Initializer implements Serializable {

	private String Path="";
	//Can be used in any os;
	private final String Default_Path=System.clearProperty("user.dir");
	
	public Initializer()
	{
	}
	public Initializer(String path)
	{
		this.Path = path;
	}
	
	public String getPath()
	{
		if(this.Path=="")
		{
			return Default_Path;
		}
		return this.Path;
	}
	
	
}
