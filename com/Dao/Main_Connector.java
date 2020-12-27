package com.Dao;
import com.Dto.*;
import java.io.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;



/*
 * 1) Used hash Map to get Fast Speed
 * 2) Using Serializable to Use less Space as it occupies low Memory
 * 3) O(1) complexity (Most of the Times) for Insert and delete and find operations 
 * 4) Used Synchronized for every Method Such that multiple threads can be used without any inconsistency
 * */



//Heart of Project
public class Main_Connector {
	
	//Commonly Used Data Members
	private String FinalPath;
	private FileInputStream fi;
	private HashMap<String,JsonObject[]> hmap;
	
	

	private String ClientID = "";
	
	public synchronized void Initializer(String path,String ClientID)
	{
		//ClientId of unique Datastore for every Client
		this.ClientID = ClientID;
		//If Unique path provided....... No problem for linux Windows it can handel all oses.
		if(path!="")
		{
			com.Dto.Initializer init1 = new com.Dto.Initializer(path);
			FinalPath = init1.getPath();
			System.out.println("Setting New Path");
		}
		//Otherwise use Default current Directory
		else {
			
			System.out.println("Setting Default Path");
			com.Dto.Initializer init1 = new com.Dto.Initializer();
			FinalPath = init1.getPath();
			
		}
		
		//To check if the dataset already exists or not if not create one otherwise loads one
		if(CheckAlreadyExists())
		{
			try {
				// used ser for serializable
				fi = new FileInputStream(new File(FinalPath+"/datastore-"+ClientID+".ser"));
	            ObjectInputStream ois = new ObjectInputStream(fi);
	            //loading existed datastore
	            hmap = (HashMap<String,JsonObject[]>) ois.readObject();
	            ois.close();
	            System.out.println("Loaded already existed datastore");
	            
			}
			catch(Exception ex)
			{
				//Not existed the datastore so create it
				System.out.println(">>>Newely created datastore<<<");
				
				//initialized new hashMap;
				hmap = new HashMap<String,JsonObject[]>();
				
				//Create new personal Client's datastore
				File myObj = new File(FinalPath+"/datastore-"+ClientID+".ser");
				try {
					if(myObj.createNewFile())
					{
						System.out.println(">>>New File created<<<");
					}
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				catch(Exception ex1)
				{
					ex1.printStackTrace();
				}
			}
		}
		else {
			try
			{
			System.out.println(">>>Newely Created Datastore<<<");
			File myObj = new File(FinalPath+"/datastore-"+ClientID+".ser");
			if(myObj.createNewFile())
			{
				System.out.println(">>>New File created<<<");
			}
			hmap = new HashMap<String,JsonObject[]>();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
	}
	
	
	//Used to check if file already exists or not
	public synchronized boolean CheckAlreadyExists()
	{
		boolean check = false;
		
		try {
			//hit and trial for file name to check its existence
			String p = FinalPath+"/datastore-"+this.ClientID;
			File temp = File.createTempFile(p,".ser");
			
			check = temp.exists();
		}
		catch(FileNotFoundException e)
		{
			System.out.println(">>>File Doesnt Exist<<<");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return check;
		
	}
	//synchronized
	public synchronized void create(String key,JsonObject[] value, int TtlSeconds)
	{
		//avoids inconsistency
		final String Key = key.toLowerCase();
		//Handled Limits of 1 gb key and 16KB values(json objects)
		if(hmap.size()>1024*1024*1024 || value.length>16*1024*1024)
		{
			System.out.println(">>>Memory Limit Exceed<<<");
			return;
		}
		//key size error handled
		if(Key.length()>32)
		{
			System.out.println(">>>Key Size Exceeded<<<");
			return;
		}
		//why to insert if already present
		if(hmap.containsKey(Key))
		{
			System.out.println(">>>Key Already Present<<<");
		}
		else {
			//adding value in datastore
			hmap.put(Key, value);
			
			/*
			 * Here handled Time to live for particular key
			 * If time gets over a callback initiates and delete the key from datastore
			 * Can also be implemented by using another approach:
			 * 		we can also create another HashMap which store key and their ttl value
			 * 		and on memory limit exceed we can check for free space and 
			 *                                      remove if not in the limits time. 
			 * */
			//Used non blocking callback
			if(TtlSeconds!=0)
			{
				Timer timer = new Timer();
				//Converting the miliseconds to seconds by x 1000.
				long timeout = 1000*TtlSeconds;
				timer.schedule(new TimerTask() {
			        @Override
			        public void run() {
			        	//go and delete the key from data store.
			            actionAfterTimeout(Key);
			        }
			    }, timeout);
			}
			//saving the state if its permenant i.e without ttl
			boolean b = saveThisState();
			if(b)
			{
				System.out.println(">>>Saved Sucessfully<<<");
			}
			else {
				System.out.print(">>>Error Occured While Saving<<<");
			}
		}
	}
	//what to do after Timeout
	public synchronized void actionAfterTimeout(String key) {
		
		
	    hmap.remove(key);

	}
	public synchronized void delete(String key)
	{
		final String Key = key.toLowerCase();
		//Delete if Key present otherwise throw appropriate error
		if(hmap.containsKey(Key))
		{
			hmap.remove(Key);
			boolean b = saveThisState();
			if(b)
			System.out.println(">>>Key removed Sucussfully<<<");
			else {
				System.out.println(">>>Some error occured<<<");
			}
		}
		else {
			System.out.println(">>>Key doesn't Exist<<<");
		}
		
	}
	public synchronized JsonObject[] response(String key)
	{
		final String Key = key.toLowerCase();
		JsonObject[] js = null;
		//Return jsonObjectArray if Key present otherwise throw appropriate error
		if(hmap.containsKey(Key))
		{
			js= hmap.get(Key);
		}
		else {
			System.out.println(">>>Key doesn't Exist<<<");
		}
		
		return js;
	}
	
	//Used to save the current state of the Datastore
	//Also Synchronized
	public synchronized boolean saveThisState()
	{
		boolean b = false;
		try
		{
			FileOutputStream f = new FileOutputStream(new File(FinalPath+"/datastore-"+this.ClientID+".ser"));
			ObjectOutputStream oos = new ObjectOutputStream(f);
            //writing Into the File
			oos.writeObject(hmap);
            oos.close();
            f.close();
            b=true;
		}
		catch(FileNotFoundException e)
		{
			System.out.println(">>>File Doesnt Exist<<<");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return b;
	}

}
