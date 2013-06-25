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

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultsActivity extends SherlockFragmentActivity implements ActivityInterface {

    private ArrayList<PostThread> threads;
    public static PostThread rootThread;
    private HttpsConnector hc;
    private SearchResultsFragment sf;
    private TextView tv;
    private ConnectionExceptionDialog connectionDialog;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String layout = sharedPref.getString("layout_pick", "default");
        if (layout.equals("default")) {
            setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
        } else if (layout.equals("dark")) {
            setTheme(R.style.Theme_Sherlock);
        } else {
            setTheme(R.style.Theme_Sherlock_Light);
        }
        super.onCreate(savedInstanceState);
        connectionDialog = new ConnectionExceptionDialog(this);
        setContentView(R.layout.activity_search_results);
        Bundle extras = getIntent().getExtras();
        hc = new HttpsConnector(this);
        HashMap<String, String> params = (HashMap<String, String>) extras.get("params");
        hc.search(params);
        sf = (SearchResultsFragment) getSupportFragmentManager().findFragmentById(R.id.search_list_fragment);
        setTitle("Search Results");
        tv = (TextView) findViewById(R.id.search_list_textview);
    }

    public void update(String jsonString) {
        if (jsonString.startsWith("Error:")) { // error in the Async Task
            connectionDialog.setMessage(jsonString);
            if (!connectionDialog.isShowing()) {
                connectionDialog.show();
            }
        } else {
            ArrayList<String> searchResults = new ArrayList<String>();
            threads = hc.getSearchFromString(jsonString);
            for (PostThread thread : threads) {
                searchResults.add(thread.toString());
            }
            if (searchResults.size() == 0) {
                tv.setText("NO RESULTS");
            } else {
                tv.setVisibility(View.GONE);
            }
            sf.update(searchResults);
        }
    }

    public void onSelectThread(int threadPosition) {
        rootThread = threads.get(0);

        for (int e = 1; e < threads.size(); e++) {
            rootThread.addChild(threads.get(e));
        }

        Intent intent = new Intent(this, PostSwipableActivity.class);
        intent.putExtra("SELECTED_NEWSGROUP", rootThread.getNewsgroup());
        intent.putExtra("SELECTED_ID", rootThread.getNumber());
        intent.putExtra("GOTO_THIS", threadPosition);
        intent.putExtra("SEARCH_RESULTS", true);

        Log.d("des", "intent made");
        startActivity(intent);
    }

    @Override
    public void onNewsgroupSelected(String newsgroupName) {

    }

}
