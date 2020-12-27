import java.io.*;
import java.util.ArrayList;

import com.Dao.*;
import com.Dto.*;


//This is the Hel[per Method to depict the Actual Client
public class Main_File {

	public static void main(String[] args) throws NumberFormatException, IOException {
		
		//Used BufferedReader for Fast IO
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Library Object to call methods
		Main_Connector obj = new Main_Connector();
		
		
		//Every Client must have unique ID , Have'nt handeled this
		System.out.println("Enter Your Unique Client ID");
		String ClientID = br.readLine();
		
		
		//Give Your Path otherwise Current Directory will be the Default path
		System.out.println("Enter 1 to Enter Your Path and 0 for default");
		
		int choice = Integer.parseInt(br.readLine());
		if(choice==1)
		{
			String Path = br.readLine();
			obj.Initializer(Path,ClientID);
		}
		else {
			obj.Initializer("",ClientID);
		}
		
		
//		System.out.println(">>>Note :- This is the Message from library<<<")
		
		//Input from Clients Thread-safety is Handled in the Library Itself by Synchronized
		while(true) {
			
		try
		{
			
		System.out.println("<<--------------Helper Function!!!!--------------->>");
		System.out.println("Enter 1 to Add Objects");
		System.out.println("Enter 2 to Show Elemets");
		System.out.println("Enter 3 to Delete Elements");
		System.out.println("Enter 4 to Exit");
		
			
			
			int x  = Integer.parseInt(br.readLine()); 
			

			if(x==4)
			{
				break;
			}
			
			//current json object-->value have 2 size of sake of simplicity and limit is 16KB
			//It's also handeled
			JsonObject js[] = new JsonObject[2];
			
			System.out.println("Enter key : ");
			
			String p = br.readLine();
			
			switch(x)
			{
				
				case 1: 
					System.out.println("Enter Json Object : ");
					//sake of simplicity-->can be replaced by array of json objects{k,v} pairs
					for(int i=0;i<2;++i)
					{
						String k  = br.readLine();
						String v = br.readLine();
						js[i] = new JsonObject(k,v);
					}
					//Optional Time to Live value
					System.out.println("Enter Invalidate time & 0 for no ttl : ");
					int ttl = Integer.parseInt(br.readLine());
					
					//creates object and also saves it(not stated ,but added the feature)
					obj.create(p, js, ttl);
					
				break;
				
				//getting Response if exists.
				case 2: JsonObject[] js1 = obj.response(p);
				
				if(js1!=null)
				{
				
					for(JsonObject i : js1)
					{
						System.out.println(i);
					}
				}
				
				break;
				//Delete object(key) if exists;
				case 3:
					obj.delete(p);
				
			}
		}
		//check if providedInput is Number or Not
		catch(NumberFormatException e)
		{
			System.out.println("Please input valid numeric value");
			continue;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			continue;
		}
		
			
	}
			

	}

}
