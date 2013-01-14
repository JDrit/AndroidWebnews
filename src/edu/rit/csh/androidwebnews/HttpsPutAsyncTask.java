
package edu.rit.csh.androidwebnews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

/**
 * The asynchronous task used to do the get and post request
 * @author JD
 */
public class HttpsPutAsyncTask extends AsyncTask<BasicNameValuePair, Integer, String> {
	WebnewsHttpClient httpclient;
	/**
	 * 
	 * @param httpclient
	 */
	public HttpsPutAsyncTask(WebnewsHttpClient httpclient) {
		this.httpclient = httpclient;   
	}
	
	/**
	 * The method that gets run when execute() is run. This sends the URL with the 
	 * to get and post variables to the server and gets the results
	 * @param params - [0] is the URL to got to, the rest are parameters to the request
	 * @return String representation of page results
	 */
	@Override
	protected String doInBackground(BasicNameValuePair... params) {
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		try {
        	HttpPut request = new HttpPut(params[0].getValue());
        	request.addHeader("accept", "application/json");
        	params = Arrays.copyOfRange(params, 1, params.length);
        	
        	for (int i = 0 ; i < params.length ; i++) {
        		nvp.add(new BasicNameValuePair(params[i].getName(), params[i].getValue()));
        	}
        	
        	request.setEntity(new UrlEncodedFormEntity(nvp));
        	
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