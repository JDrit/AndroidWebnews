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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * This is used to display all the threads from a particular newsgroup
 */
public class DisplayThreadsActivity extends BaseActivity {

    public String newsgroupName;
    private InvalidApiKeyDialog dialog;
    public ArrayList<PostThread> threadsDirectMap;
    private ConnectionExceptionDialog connectionDialog;
    static public ArrayList<PostThread> lastFetchedThreads;
    NewsgroupListMenu newsgroupListMenu;
    public boolean requestedAdditionalThreads = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastFetchedThreads = new ArrayList<PostThread>();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        hc = new HttpsConnector(this);
        dialog = new InvalidApiKeyDialog(this);
        Bundle extras = getIntent().getExtras();
        connectionDialog = new ConnectionExceptionDialog(this);
        if (extras != null) {
            newsgroupName = extras.getString("SELECTED_NEWSGROUP");
        } else {
            newsgroupName = "none";
        }

        threadsDirectMap = new ArrayList<PostThread>();

        newsgroupListMenu = new NewsgroupListMenu(this);
        newsgroupListMenu.checkEnabled();

        setContentView(R.layout.displaythreads_activity);

        if (!sharedPref.getString("newsgroups_json_string", "").equals("")) {
            newsgroupListMenu.update(hc.getNewsGroupFromString(sharedPref.getString("newsgroups_json_string", "")));
            hc.startUnreadCountTask();
        } else {
            hc.getNewsGroups();
        }

        hc.getNewsgroupThreads(newsgroupName, 20);
        setTitle(newsgroupName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.displaythreads_menu, menu);
        // Get the SearchView and set the searchable configuration
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                Intent intent = new Intent(DisplayThreadsActivity.this, SearchResultsActivity.class);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("keywords", query);
                intent.putExtra("params", params);
                startActivity(intent);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    public void viewPost(int item) {
        if (item >= threadsDirectMap.size())
            return;
        PostThread thread = threadsDirectMap.get(item);
        PostThread selected = thread;

        while (thread.getParent() != null)
            thread = thread.getParent();

        Intent intent = new Intent(this, PostSwipeableActivity.class);
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
                Intent myIntent = new Intent(this, ComposeActivity.class);
                myIntent.putExtra("NEWSGROUP", newsgroupName);
                startActivity(myIntent);
                return true;

            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.menu_refresh:
                hc.getNewest(true);
                hc.getNewsGroups();
                return true;

            case R.id.menu_about:
                startActivity(new Intent(this, InfoActivity.class));
                return true;

            case R.id.menu_adv_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;

           case R.id.menu_mark_all_read:
                hc.markRead();
                hc.getNewest(false);
                hc.getNewsGroups();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, SearchActivity.class));
        return super.onSearchRequested();
    }

    @Override
    public void update(String jsonString) {
        if (jsonString.startsWith("Error:")) { // error in the Async Task
            connectionDialog.setMessage(jsonString);
            if (!connectionDialog.isShowing()) {
                connectionDialog.show();
            }
        } else {
            try {
                JSONObject obj = new JSONObject(jsonString);
                if (obj.has("error")) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                } else if (obj.has("posts_older")) {
                    if (hc.getThreadsFromString(jsonString).size() == 0) {
                        //hitBottom = true;
                        ((DisplayThreadsFragment) getSupportFragmentManager().
                                findFragmentById(R.id.threadsfragment)).addThreads(new ArrayList<PostThread>());
                    } else if (!requestedAdditionalThreads) {
                        ArrayList<PostThread> threads = hc.getThreadsFromString(jsonString);
                        lastFetchedThreads.clear();
                        lastFetchedThreads = (ArrayList<PostThread>) threads.clone();
                        ((DisplayThreadsFragment) getSupportFragmentManager().findFragmentById(R.id.threadsfragment)).
                                update(threads);
                    } else {
                        ArrayList<PostThread> newThreads = hc.getThreadsFromString(jsonString);
                        for (PostThread thread : newThreads)
                            lastFetchedThreads.add(thread);
                        ((DisplayThreadsFragment) getSupportFragmentManager().findFragmentById(R.id.threadsfragment)).
                                addThreads(newThreads);
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
            } catch (JSONException ignored) {
            }
        }
    }

    @Override
    public void onNewsgroupSelected(String newsgroupName) {
        if (!newsgroupName.equals(this.newsgroupName)) {
            Intent myIntent = new Intent(DisplayThreadsActivity.this, DisplayThreadsActivity.class);
            myIntent.putExtra("SELECTED_NEWSGROUP", newsgroupName);
            startActivity(myIntent);
            finish();
        }
    }

    @Override
    public void onRestart() { // throwing issue when we try to call any hc.get..., need to fix for updating newsgroups
        onResume();
        hc.startUnreadCountTask();
        hc.getNewsgroupThreads(newsgroupName, 20, false);
    }
}