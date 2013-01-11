package edu.rit.csh.androidwebnews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class HttpsConnector extends AsyncTask<String, Integer, String> {
	String apiKey;
	String mainUrl = "https://webnews.csh.rit.edu";
	WebnewsHttpClient httpclient;
	
	public HttpsConnector(String apiKey, Activity activity) {
		this.apiKey = apiKey;
		httpclient = new WebnewsHttpClient(activity.getApplicationContext());
	}
	
	public ArrayList<Newsgroup> getNewsGroups() {
        return null;
		
	}
	
	public ArrayList<Thread> getUnread() {
		return null;
	}
	
	public ArrayList<Thread> getNewsgroupThreads(String name, int amount) {
		return null;
	}
	
	public String getPostBody(String newsgroup, int id) {
		return null;
	}
	
	public ArrayList<Thread> getNewsgroupThreadsDate(String date, int amount) {
		return null;
	}
	
	public void markRead() {
		
	}
	
	public void markRead(String name) {
		
	}
	
	private ArrayList<Thread> makeThreads(String jsonObj) {
		return null;
	}
	
	private String addApiToUrl(String url) {
		if (!url.endsWith("?")) {
			url += "?";
		}
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("api_key", apiKey));
		params.add(new BasicNameValuePair("api_agent", "Android_Webnews"));
		String paramString = URLEncodedUtils.format(params,  "utf-8");
		url += paramString;
		return url;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
        	HttpGet request = new HttpGet(mainUrl);
			Log.d("AndroidWebnews", mainUrl);
			HttpResponse response = httpclient.execute(request);
			HttpEntity responseEntity = response.getEntity();
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            
            while ((line = in.readLine()) != null) {
            	Log.d("AndroidWebnews", line);
                sb.append(line + NL);
            }
            in.close();
            String page = sb.toString();
            Log.d("test", page);
    		return page;
            
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	
}
