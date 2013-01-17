
package edu.rit.csh.androidwebnews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/**
 * The asynchronous task used to do the get and post request
 * @author JD
 */
public class HttpsGetAsyncTask extends AsyncTask<String, Integer, String> {
	WebnewsHttpClient httpclient;
	Activity activity;
	boolean showProgress;
	callBack callback;
	ProgressDialog p;
	/**
	 * 
	 * @param httpclient
	 */
	public HttpsGetAsyncTask(WebnewsHttpClient httpclient, boolean showProgress, Activity activity) {
		this.httpclient = httpclient;
		this.activity = activity;
		this.showProgress = showProgress;
	}

    protected void onPreExecute() {
    	 super.onPreExecute();
    	 
    	 if (showProgress) {
	         p = new ProgressDialog(activity);
	         p.setTitle("Fetching Info");
	         p.setMessage("Contacting Server...");
	        // p.setCancelable(false);
	         p.show();
	         
	         Log.d("jddebug", "p started");
    	 }
     }
    
     
     
	/**
	 * The method that gets run when execute() is run. This sends the URL with the 
	 * to get and post variables to the server and gets the results
	 * @param params - [0] is the URL to got to
	 * @return String representation of page results
	 */
	@Override
	protected String doInBackground(String... params) {
		Log.d("jddebug", "back started");
		try {
        	HttpGet request = new HttpGet(params[0]);
        	request.addHeader("accept", "application/json");
        	
			HttpResponse response = httpclient.execute(request);
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String page = sb.toString();
            Log.d("jddebug", "back ended");
    		return page;
            
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("jddebug", "back ended");
		return "";
	}
	
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
		if (showProgress) {
			p.dismiss();
			Log.d("jddebug", "p ended");
			Log.d("jddebug", activity.getLocalClassName());
			((DisplayThreadsActivity) activity).update(s);
		}
		
		Log.d("jddebug", activity.getLocalClassName());
	}
		

}