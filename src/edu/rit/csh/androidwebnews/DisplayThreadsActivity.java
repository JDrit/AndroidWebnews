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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This is used to display all the threads from a particular newsgroup
 */
public class DisplayThreadsActivity extends FragmentActivity implements ActivityInterface {
	
	public String newsgroupName;
	private InvalidApiKeyDialog dialog;
	public ArrayList<PostThread> threadsDirectMap;
	static public ArrayList<PostThread> lastFetchedThreads;
	private DisplayThreadsFragment dtf;
	private HttpsConnector hc;
	NewsgroupListMenu newsgroupListMenu;
	public boolean requestedAdditionalThreads = false;
	private SharedPreferences sharedPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        lastFetchedThreads = new ArrayList<PostThread>();
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);	    
	    hc = new HttpsConnector(this);
	    dialog = new InvalidApiKeyDialog(this);
		Bundle extras = getIntent().getExtras();
		
		if(extras != null)
		{
			newsgroupName = extras.getString("SELECTED_NEWSGROUP");
		}
		else
		{
			newsgroupName = "none";
		}
		
		threadsDirectMap = new ArrayList<PostThread>();
		
		newsgroupListMenu = new NewsgroupListMenu(this);
		newsgroupListMenu.checkEnabled();
		
		setContentView(R.layout.displaythreads_activity);
		
		dtf = (DisplayThreadsFragment)getSupportFragmentManager().findFragmentById(R.id.threadsfragment);
		
		if (!sharedPref.getString("newsgroups_json_string", "").equals("")) {
			newsgroupListMenu.update(hc.getNewsGroupFromString(sharedPref.getString("newsgroups_json_string", "")));
			hc.startUnreadCountTask();
		} else {
			hc.getNewsGroups();
		}
		
		hc.getNewsgroupThreads(newsgroupName, 20);
		setTitle(newsgroupName + " newsgroup");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_displaythreads_menu, menu);
		return true;
	}
	
	public void viewPost(int item) {
		PostThread thread = threadsDirectMap.get(item);
		PostThread selected = thread;
		
		while(thread.getParent() != null)
			thread=thread.getParent();
		
		Intent intent = new Intent(this, PostSwipableActivity.class);
		intent.putExtra("SELECTED_NEWSGROUP", thread.getNewsgroup());
		intent.putExtra("SELECTED_ID", thread.getNumber());
		intent.putExtra("GOTO_THIS", threadsDirectMap.indexOf(selected) - threadsDirectMap.indexOf(thread));

		Log.d("des", "intent made");
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		switch (item.getItemId()) {
		case R.id.new_post:
            Intent intent = new Intent(this, ComposeActivity.class);
            intent.putExtra("NEWSGROUP", newsgroupName);
			startActivity(intent);
			return true;
		
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		
		case R.id.menu_refresh:
			hc.getNewsgroupThreads(newsgroupName, 20);
			hc.getNewsGroups();
			return true;
			
		case R.id.menu_about:
			startActivity(new Intent(this, InfoActivity.class));
			return true;
			
		case R.id.menu_search:
			startActivity(new Intent(this, SearchActivity.class));
			return true;
		
		case R.id.menu_mark_all_read:
			hc.markRead(newsgroupName);
			hc.getNewsgroupThreads(newsgroupName, 20, false);
			hc.getNewsGroups();
			
			return true;
		}
		return false;
	}
	
	@Override
	public void update(String jsonString) {
        try {
            dtf = (DisplayThreadsFragment) getSupportFragmentManager().findFragmentById(R.id.threadsfragment);
            JSONObject obj = new JSONObject(jsonString);
            if (obj.has("error")) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            } else if (obj.has("posts_older")) {
                if(hc.getThreadsFromString(jsonString).size() == 0) {
                    //hitBottom = true;
                    dtf.addThreads(new ArrayList<PostThread>());
                } else if(!requestedAdditionalThreads) {
                    ArrayList<PostThread> threads = hc.getThreadsFromString(jsonString);
                    lastFetchedThreads.clear();
                    lastFetchedThreads = (ArrayList<PostThread>) threads.clone();
                    dtf.update(threads);
                } else {
                    ArrayList<PostThread> newThreads = hc.getThreadsFromString(jsonString);
                    for(PostThread thread : newThreads)
                        lastFetchedThreads.add(thread);
                    dtf.addThreads(newThreads);
                    requestedAdditionalThreads = false;
                }
            } else if (obj.has("unread_counts")) {  // unread count
                int unread = hc.getUnreadCountFromString(jsonString)[0];
                int groupUnread = 0;
                for (Newsgroup group : hc.getNewsGroupFromString(sharedPref.getString("newsgroups_json_string", ""))) {
                    groupUnread += group.unreadCount;
                }
                if (unread != groupUnread) {
                    hc.getNewsGroups();
                } else {
                    newsgroupListMenu.update(hc.getNewsGroupFromString(sharedPref.getString("newsgroups_json_string", "")));
                }
            } else {  // newsgroups
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("newsgroups_json_string", jsonString);
                editor.commit();
                newsgroupListMenu.update(hc.getNewsGroupFromString(jsonString));
            }
        } catch (JSONException e) {
        }
	}

	@Override
	public void onNewsgroupSelected(String newsgroupName) {
		if(!newsgroupName.equals(this.newsgroupName))
		{
			Intent myIntent = new Intent(DisplayThreadsActivity.this, DisplayThreadsActivity.class);
			myIntent.putExtra("SELECTED_NEWSGROUP", newsgroupName);
			startActivity(myIntent);
			finish();
		}
	}
	
	
	@Override
	public void onRestart() { // throwing issue when we try to call any hc.get..., need to fix for updating newsgroups
		super.onResume();
		hc.startUnreadCountTask();
		hc.getNewsgroupThreads(newsgroupName, 20, false);
	}
}