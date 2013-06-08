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

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * The asynchronous task used to do the POST requests
 *
 * @author JD
 */
class HttpsPostAsyncTask extends AsyncTask<BasicNameValuePair, Integer, String> {
    private final WebnewsHttpClient httpclient;


    public HttpsPostAsyncTask(WebnewsHttpClient httpclient) {
        this.httpclient = httpclient;
    }


    /**
     * The method that gets run when execute() is run. This sends the URL with the
     * POST parameters to the server and gets the results
     *
     * @param params - [0] is the URL to got to, the rest are parameters to the request
     * @return String representation of page results
     */
    @Override
    protected String doInBackground(BasicNameValuePair... params) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
        String line;
        try {
            HttpPost request = new HttpPost(params[0].getValue());
            request.addHeader("accept", "application/json");
            //params = Arrays.copyOfRange(params, 1, params.length);
            for (BasicNameValuePair pair : params) {
                nvp.add(new BasicNameValuePair(pair.getName(), pair.getValue()));
            }

            request.setEntity(new UrlEncodedFormEntity(nvp));

            HttpResponse response = httpclient.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder sb = new StringBuilder("");

            String NL = System.getProperty("line.separator");

            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            in.close();
            String page = sb.toString();
            Log.d("jd - post result", " : " + page);
            return page;

        } catch (ClientProtocolException e) {
            Log.d("jd - post error", e.toString());
        } catch (ConnectTimeoutException e) {
            Log.d("jd - post timeout error ", e.toString());
            doInBackground(params);
        } catch (IOException e) {
            Log.d("jd - post error", e.toString());
        }
        return "";
    }


}