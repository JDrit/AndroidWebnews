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

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class SearchActivity extends FragmentActivity implements ActivityInterface {
	private HttpsConnector hc;
	private SearchFragment sf;
	private InvalidApiKeyDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		dialog = new InvalidApiKeyDialog(this);	    
	    hc = new HttpsConnector(this);	    	    
	    hc.getNewsGroups(); // used for list of newsgroups to look through
	    sf = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
	}
	
	@Override
	public void update(String jsonString) {
		JSONObject obj;
		try {
			obj = new JSONObject(jsonString);
			if (obj.has("error")) {
				if (!dialog.isShowing()) {
					dialog.show();
				}
			} else if(obj.has("newsgroups")) {
				sf.update(hc.getNewsGroupFromString(jsonString));
			}
			
		} catch (JSONException e) {
			
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
 