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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.DatePicker;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends SherlockFragmentActivity implements ActivityInterface {
    private HttpsConnector hc;
    private SearchFragment sf;
    private InvalidApiKeyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        DatePicker startDate;
        setContentView(R.layout.activity_search);
        dialog = new InvalidApiKeyDialog(this);
        hc = new HttpsConnector(this);
        hc.getNewsGroups(); // used for list of newsgroups to look through
        sf = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        startDate = (DatePicker) findViewById(R.id.search_datePicker1);
        startDate.init(startDate.getYear() - 1, startDate.getMonth(), startDate.getDayOfMonth(), null);
        setTitle("Search");
    }

    @Override
    public void update(String jsonString) {
        JSONObject obj;
        if (jsonString.startsWith("Error:")) { // error in the Async Task
            ConnectionExceptionDialog dialog = new ConnectionExceptionDialog(this, jsonString);
            dialog.show();
        } else {
            try {
                obj = new JSONObject(jsonString);
                if (obj.has("error")) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                } else if (obj.has("newsgroups")) {
                    sf.update(hc.getNewsGroupFromString(jsonString));
                }

            } catch (JSONException ignored) {

            }
        }
    }

    @Override
    public void onNewsgroupSelected(String newsgroupName) {

    }

    public void search(View view) {
        Intent myIntent = new Intent(SearchActivity.this, SearchResultsActivity.class);
        myIntent.putExtra("params", sf.getParams());
        startActivity(myIntent);
    }
}
 