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

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;

public class SearchResultsActivity extends FragmentActivity implements ActivityInterface {
	
	private ArrayList<PostThread> threads;
	public static PostThread rootThread;
	private HttpsConnector hc;
	private SearchResultsFragment sf;

	@Override
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		Bundle extras = getIntent().getExtras();
		hc = new HttpsConnector(this);
		HashMap<String, String> params = (HashMap<String, String>) extras.get("params");
		hc.search(params);
		sf = (SearchResultsFragment) getSupportFragmentManager().findFragmentById(R.id.search_list_fragment);
	}

	public void update(String jsonString) {
		ArrayList<String> searchResults = new ArrayList<String>();
		threads = hc.getSearchFromString(jsonString);
		for(PostThread thread : threads) {
			searchResults.add(thread.toString());
		}
		sf.update(searchResults);
		
	}
	
	public void onSelectThread(int threadPosition)
	{
		rootThread = threads.get(0);
		
		for(int e = 1; e < threads.size(); e++)
		{
			rootThread.children.add(threads.get(e));
		}
		
		Intent intent = new Intent(this, PostSwipableActivity.class);
		intent.putExtra("SELECTED_NEWSGROUP", rootThread.newsgroup);
		intent.putExtra("SELECTED_ID", rootThread.number);
		intent.putExtra("GOTO_THIS", threadPosition);
		intent.putExtra("SEARCH_RESULTS", true);

		Log.d("des", "intent made");
		startActivity(intent);
	}

	@Override
	public void onNewsgroupSelected(String newsgroupName) {
		
	}

}
