package edu.rit.csh.androidwebnews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import android.os.AsyncTask;

/**
 * The asynchronous task used to do the get and post request
 * @author JD
 */
public class HttpsAsyncTask extends AsyncTask<String, Integer, String> {
	WebnewsHttpClient httpclient;
	/**
	 * 
	 * @param httpclient
	 */
	public HttpsAsyncTask(WebnewsHttpClient httpclient) {
		this.httpclient = httpclient;   
	}
	
	/**
	 * The method that gets run when execute() is run. This sends the URL with the 
	 * to get and post variables to the server and gets the results
	 * @param params - [0] is the URL to got to
	 * @return String representation of page results
	 */
	@Override
	protected String doInBackground(String... params) {
		try {
        	HttpGet request = new HttpGet(params[0]);
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
    		return page;
            
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	

}
