/**
See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  This code is licensed
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/	
package edu.rit.csh.androidwebnews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/**
 * The asynchronous task used to do the get and post request
 * @author JD
 */
public class HttpsGetAsyncTask extends AsyncTask<URI, Integer, String> {
	WebnewsHttpClient httpclient;
	Activity activity;
	boolean showProgress;
	
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
	         p.setCancelable(false);
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
	protected String doInBackground(URI... params) {
		Log.d("jddebug", "back started");
		Log.d("URI", params[0].toString());
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
            Log.d("jddebug-getasynctask", params[0] + " : " + page);
    		return page;
            
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.d("jddebug-getasynctask", e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("jddebug-getasynctask", e.toString());
		}
		Log.d("jddebug", "back ended");
		//Log.d("jddebug-getasynctask", params[0] + " : " + page);
		return "";
	}
	
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
		if (showProgress) {
			p.dismiss();
			Log.d("jddebug", "p ended");
			Log.d("jddebug", activity.getLocalClassName());
		}
		
		if(activity != null)
			((ActivityInterface) activity).update(s);
		
	}
		

}