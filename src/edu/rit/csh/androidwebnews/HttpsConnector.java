package edu.rit.csh.androidwebnews;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.util.Log;

/**
 * The object that the app interfaces with to get all the information 
 * to and from webnews 
 * @author JD
 */
public class HttpsConnector {
	String mainUrl = "https://webnews.csh.rit.edu";
	String apiKey;
	WebnewsHttpClient httpclient;
	
	public HttpsConnector(String apiKey, Activity activity) {
		httpclient = new WebnewsHttpClient(activity.getApplicationContext());
		this.apiKey = apiKey;
	}
	
	/**
	 * Gets a list of Newsgroup objects that represent the current newsgroup on webnews.
	 * @return ArrayList<Newsgroup> - the current newsgroups located on webnews, null if
	 * there was an error in the procedure.
	 */
	public ArrayList<Newsgroup> getNewsGroups() {
		ArrayList<Newsgroup> newsgroups = new ArrayList<Newsgroup>();
		String url = addApiToUrl(mainUrl + "/newsgroups");
		Log.d("url", url);
		try {
			 try {
				JSONObject jObj = new JSONObject(new HttpsAsyncTask(httpclient).execute(url).get());
				JSONArray jArray = new JSONArray(jObj.getString("newsgroups"));
				for (int i = 0 ; i < jArray.length() ; i++) {
					newsgroups.add(new Newsgroup(new JSONObject(jArray.getString(i)).getString("name"),
							new JSONObject(jArray.getString(i)).getInt("unread_count"),
							new JSONObject(jArray.getString(i)).getString("unread_class")));
				}
				return newsgroups;
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
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



	
}
