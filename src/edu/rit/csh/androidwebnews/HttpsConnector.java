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
		String url = formatUrl(mainUrl + "/newsgroups", new LinkedList<NameValuePair>());
		Log.d("jsonurl", url);
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
			Log.d("jsonError", "JSONException");
		} catch (InterruptedException e) {
			Log.d("jsonError", "InterruptedException");
		} catch (ExecutionException e) {
			Log.d("jsonError", "ExecutionException");
		}
		return null;
	}
	
	/**
	 * Gets the newest threads on webnews to display on the front page
	 * @return ArrayList<Thread> - list of the 20 newest or sticky threads
	 */
	public ArrayList<Thread> getNewest() {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		String url = formatUrl(mainUrl + "/activity", new LinkedList<NameValuePair>());
		try {
			JSONObject jObj = new JSONObject(new HttpsAsyncTask(httpclient).execute(url).get());
			Log.d("json", "1");
			JSONArray jArray = jObj.getJSONArray("activity");
			Log.d("json", "1");
			for (int i = 0 ; i < jArray.length() ; i++) {
				JSONObject jAct = jArray.getJSONObject(i).getJSONObject("thread_parent");
				threads.add(new Thread(jAct.getString("date"), 
						jAct.getInt("number"), 
						jAct.getString("subject"),
						jAct.getString("author_name"),
						jAct.getString("author_email"),
						jAct.getString("newsgroup"),
						false,
						"",
						""));
			}
			return threads;
		} catch (JSONException e) {
			Log.d("jsonError", "JSONException");
		} catch (InterruptedException e) {
			Log.d("jsonError", "InterruptedException");
		} catch (ExecutionException e) {
			Log.d("jsonError", "ExecutionException");
		}
		return null;
	}
	
	/**
	 * Gets the threads for a certain newsgroup. All the threads have an ArrayList
	 * of their sub-threads.
	 * @param name - the name of the newsgroup
	 * @param amount - the amount of threads to return, has to be <= 20, -1 == default of 10 
	 * @return ArrayList<Thread> - list of the top level threads for the newsgroup
	 */
	public ArrayList<Thread> getNewsgroupThreads(String name, int amount) {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		if (amount == -1) {
			amount = 10;
		} else if (amount > 20) {
			amount = 20;
		}
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("limit", new Integer(amount).toString()));
		params.add(new BasicNameValuePair("thread_mode", "normal"));
		String url = formatUrl(mainUrl + "/" + name + "/index", params);
		try {
			
			JSONObject  jObj = new JSONObject(new HttpsAsyncTask(httpclient).execute(url).get());
			JSONArray jArray = new JSONArray(jObj.getString("posts_older"));
			for (int i = 0 ; i < jArray.length() ; i++) {
				threads.add(createThread(new JSONObject(jArray.getString(i))));
			}
			return threads;
		} catch (JSONException e) {
			Log.d("jsonError", "JSONException");
		} catch (InterruptedException e) {
			Log.d("jsonError", "InterruptedException");
		} catch (ExecutionException e) {
			Log.d("jsonError", "ExecutionException");
		}
		return null;
	}
	
	/**
	 * Gets the post body for the post specified. This takes a newsgroup name and a
	 * post ID number to find the post's body
	 * @param newsgroup - the newsgroup name
	 * @param id - the number of the post
	 * @return String - the body of the post
	 */
	public String getPostBody(String newsgroup, int id) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("mark_read", "True"));
		String url = formatUrl(mainUrl + "/" + newsgroup + "/" + id, params);
		try {
			JSONObject jObj = new JSONObject(new HttpsAsyncTask(httpclient).execute(url).get());
			JSONObject jsonPost = jObj.getJSONObject("post");
			String body = jsonPost.getString("body");
			return body;			
		} catch (JSONException e) {
			Log.d("jsonError", "JSONException");
		} catch (InterruptedException e) {
			Log.d("jsonError", "InterruptedException");
		} catch (ExecutionException e) {
			Log.d("jsonError", "ExecutionException");
		}
		return null;
	}
	
	/**
	 * Gets the threads for a certain newsgroup past a certain date. This is done
	 * to work around the 20 max thread return of the webnews api. All of the threads
	 * contain an ArrayList of their sub-threads
	 * @param newsgroup - the newsgroup name
	 * @param date - the date to get post older than
	 * @param amount - the amount of threads to return
	 * @return ArrayList<Thread> - a list of the threads in the newsgroup from the starting
	 * date
	 */
	public ArrayList<Thread> getNewsgroupThreadsByDate(String newsgroup, String date, int amount) {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		if (amount == -1) {
			amount = 10;
		} else if (amount > 20) {
			amount = 20;
		}
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("limit", new Integer(amount).toString()));
		params.add(new BasicNameValuePair("thread_mode", "normal"));
		params.add(new BasicNameValuePair("from_older", date));
		String url = formatUrl(mainUrl + "/" + newsgroup + "/index", params);
		
		try {
			
			JSONObject  jObj = new JSONObject(new HttpsAsyncTask(httpclient).execute(url).get());
			JSONArray jArray = new JSONArray(jObj.getString("posts_older"));
			for (int i = 0 ; i < jArray.length() ; i++) {
				threads.add(createThread(new JSONObject(jArray.getString(i))));
			}
			return threads;
		} catch (JSONException e) {
			Log.d("jsonError", "JSONException");
		} catch (InterruptedException e) {
			Log.d("jsonError", "InterruptedException");
		} catch (ExecutionException e) {
			Log.d("jsonError", "ExecutionException");
		}
		return null;
	}

	public void markRead() {

	}

	public void markRead(String name) {

	}

	private ArrayList<Thread> makeThreads(String jsonObj) {
		return null;
	}
	
	/**
	 * Formats the URL String with the API key and all the extra parameters
	 * @param url - the url to format
	 * @param addOns - List<NameValuePair> of extra parameters, can be empty
	 * @return String - the formated String
	 */
	private String formatUrl(String url, List<NameValuePair> addOns) {
		if (!url.endsWith("?")) {
			url += "?";
		}
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("api_key", apiKey));
		params.add(new BasicNameValuePair("api_agent", "Android_Webnews"));
		if (addOns.size() != 0) {
			params.addAll(addOns);
		}
		
		String paramString = URLEncodedUtils.format(params,  "utf-8");
		url += paramString;
		return url;
	}
	
	/**
	 * Creates the threads with all of their sub-threads
	 * @param obj - the JSONObject of the top level thread to use
	 * @return Thread - the newly created thread with all of its sub-threads
	 */
	private Thread createThread(JSONObject obj) {
		JSONObject post;
		try {
			post = new JSONObject(obj.getString("post"));
			Thread thread = new Thread(post.getString("date"), 
					post.getInt("number"), 
					post.getString("subject"),
					post.getString("author_name"),
					post.getString("author_email"),
					post.getString("newsgroup"),
					post.getBoolean("starred"),
					post.getString("unread_class"),
					post.getString("personal_class"));
			
			if (obj.getJSONArray("children") != null ) {
				for (int i = 0 ; i < obj.getJSONArray("children").length() ; i++) {
					thread.children.add(createThread(obj.getJSONArray("children").getJSONObject(i)));
				}
			}
			return thread;		
		} catch (JSONException e) {
			Log.d("jsonError", "JSONException");
		}
		return null;
	}

}