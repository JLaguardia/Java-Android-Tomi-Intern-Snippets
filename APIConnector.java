package com.tomitg.www;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

//this class goes above the minimum specified sdk ver. We will not use this.
public class APIConnector {
	//127.0.0.1 == local host
	
	public JSONArray getNames(){
		String url = "http://127.0.0.1/C:/Users/James/Documents/conTest/testScript.php";
		
		HttpEntity http = null;
		
		try{
			DefaultHttpClient httpCli = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			
			HttpResponse httpResponse = httpCli.execute(httpGet);
			
			http = httpResponse.getEntity();
		}catch(ClientProtocolException cpe){
			cpe.printStackTrace();
		}catch(IOException ie){
			ie.printStackTrace();
		}
		
		JSONArray jsonArray = null;
		
		if(http != null){
			try{
				String entityResponse = EntityUtils.toString(http);
				//Log.e("Entity Response: ", entityResponse);
				
				jsonArray = new JSONArray(entityResponse);
			}catch(JSONException je){
				je.printStackTrace();
			}catch(IOException ie){
				ie.printStackTrace();
			}
		}
		return jsonArray;
	}
}