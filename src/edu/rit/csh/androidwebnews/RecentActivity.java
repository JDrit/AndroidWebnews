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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RecentActivity extends SherlockFragmentActivity implements ActivityInterface {
    private InvalidApiKeyDialog dialog;
    private ConnectionExceptionDialog connectionDialog;
    private RecentFragment rf;
    private HttpsConnector hc;
    private NewsgroupListMenu newsgroupListMenu;
    private SharedPreferences sharedPref;
    private FirstTimeDialog ftd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String layout = sharedPref.getString("layout_pick", "default");
        if (layout.equals("default")) {
            setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
        } else if (layout.equals("dark")) {
            setTheme(R.style.Theme_Sherlock);
        } else {
            setTheme(R.style.Theme_Sherlock_Light);
        }
        super.onCreate(savedInstanceState);
        newsgroupListMenu = new NewsgroupListMenu(this);

        newsgroupListMenu.checkEnabled();
        hc = new HttpsConnector(this);
        dialog = new InvalidApiKeyDialog(this);
        connectionDialog = new ConnectionExceptionDialog(this);
        ftd = new FirstTimeDialog(this);

        setContentView(R.layout.activity_recent);

        rf = (RecentFragment) getSupportFragmentManager().findFragmentById(R.id.recent_fragment);

        if (!sharedPref.getBoolean("first_time", true)) {
            hc.getNewest(true);
            if (!sharedPref.getString("newsgroups_json_string", "").equals("")) {
                newsgroupListMenu.update(hc.getNewsGroupFromString(sharedPref.getString("newsgroups_json_string", "")));
                hc.startUnreadCountTask();
            } else {
                hc.getNewsGroups();
            }

            Intent intent = new Intent(this, UpdaterService.class);
            PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            // if the run service is selected, an alarm is started to repeat over given time
            if (sharedPref.getBoolean("run_service", false)) {
                String timeString = sharedPref.getString("time_between_checks", "15");
                int time = 15;
                if (!timeString.equals("")) {
                    time = Integer.valueOf(timeString);
                }
                alarm.cancel(pintent);
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), time * 60000, pintent);
            } else {
                alarm.cancel(pintent);
            }
        } else {

            ftd.show();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("first_time", false);
            editor.commit();


        }
        setTitle("Recent Posts");


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
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                Intent intent = new Intent(RecentActivity.this, SearchResultsActivity.class);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("keywords", query);
                intent.putExtra("params", params);
                startActivity(intent);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_post:
                Intent myIntent = new Intent(this, ComposeActivity.class);
                //myIntent.putExtra("NEWSGROUP", newsgroupName);
                startActivity(myIntent);
                break;

            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.menu_refresh:
                hc.getNewest(true);
                hc.getNewsGroups();
                break;

            case R.id.menu_about:
                startActivity(new Intent(this, InfoActivity.class));
                break;

            case R.id.menu_adv_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;

            case R.id.menu_mark_all_read:
                hc.markRead();
                hc.getNewest(false);
                hc.getNewsGroups();
                break;


        }
        return false;
    }

    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, SearchActivity.class));
        return super.onSearchRequested();
    }


    /**
     * This is called to by the async task when there is an fragment to update.
     * This then updates the correct fragment based on what is given. The options
     * are: show an error dialog, update recent posts, update newgroups, and update
     * unread counts. When unread counts is updated and is different from what it used
     * to be, then it updates the newsgroups to get the correct display.
     *
     * @param jsonString - a string representing the json object of the data
     */
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
                if (obj.has("error")) { // invalid api key
                    if (!dialog.isShowing()) {
                        if (!ftd.isShowing()) {
                            dialog.show();
                        }
                    }
                } else if (obj.has("activity")) { // recent
                    Log.d("string", hc.getNewestFromString(jsonString).toString());
                    Log.d("string", rf.toString());
                    rf.update(hc.getNewestFromString(jsonString));
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
                e.printStackTrace();
            }
        }
    }

    public void onNewsgroupSelected(final String newsgroupName) {
        Intent myIntent = new Intent(RecentActivity.this, DisplayThreadsActivity.class);
        myIntent.putExtra("SELECTED_NEWSGROUP", newsgroupName);
        startActivity(myIntent);
    }


    @Override
    public void onRestart() {
        super.onResume();
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        hc.getNewest(false);

        if (!sharedPref.getString("newsgroups_json_string", "").equals("")) {
            newsgroupListMenu.update(hc.getNewsGroupFromString(sharedPref.getString("newsgroups_json_string", "")));
            hc.startUnreadCountTask();
        } else {
            hc.getNewsGroups();
        }

        NewsgroupListMenu.menuShown = false;
        /*finish();
        Intent intent = new Intent(this, RecentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }

    public NewsgroupListMenu getNewsgroupListMenu() {
        return (newsgroupListMenu);
    }

}