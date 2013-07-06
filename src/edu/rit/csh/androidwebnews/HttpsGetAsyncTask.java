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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * The asynchronous task used to do the GET requests. Displays a progress bar
 * while the task is running, if told to
 * URI: the URI that is formatted to give the WebNews request, This will be sent to the server to
 * get the given information
 * Integer:
 */
class HttpsGetAsyncTask extends AsyncTask<URI, Void, String> {
    private final WebnewsHttpClient httpclient;
    private final Activity activity;
    private final boolean showProgress;

    private ProgressDialog p;

    /**
     * Constructor for the async task to do GET requests
     *
     * @param httpclient   - the client to use
     * @param showProgress - true to show progress dialog, false otherwise
     * @param activity     - the activity that called the task
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
            p.setIndeterminateDrawable(activity.getResources().getDrawable(R.anim.progress));
            p.setTitle("Fetching Info");
            p.setMessage("Contacting Server...");
            p.setCancelable(false);
            p.show();
        }
    }

    /**
     * The method that gets run when execute() is run. This sends the URL with the
     * GEt parameters to the server and gets the results
     *
     * @param params - [0] is the URL to got to
     * @return String representation of page results
     */
    @Override
    protected String doInBackground(URI... params) {
        String page;
        BufferedReader in;
        HttpResponse response;
        HttpGet request;

        try {

            request = new HttpGet(params[0]);
            request.addHeader("accept", "application/json");

            response = httpclient.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder sb = new StringBuilder("");
            String line;
            String NL = System.getProperty("line.separator");

            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            page = sb.toString();
            in.close();
            Log.d("jd - getAsync response", page);
        } catch (ClientProtocolException e) {
            return ("Error: Client Exception while connecting to " + params[0].getHost());
        } catch (HttpHostConnectException e) {
            return ("Error: Could not connect to " + params[0].getHost() + ". Mostly likely caused by the server not being up");
        } catch (ConnectTimeoutException e) { // redoes the request if there is a timeout
            Log.d("jd - getAsync Error", e.toString());
            return ("Error: Connection timeout");
        } catch (IOException e) {
            Log.d("jd - GetAsync Error", e.toString());
            return ("Error: IO Connection exception while connection to " + params[0].getHost());
        }

        return page;
    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (showProgress) {
            p.dismiss();
        }

        if (activity != null)
            ((BaseActivity) activity).shouldUpdate(s);

    }


}