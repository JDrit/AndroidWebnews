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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import org.json.JSONException;
import org.json.JSONObject;

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
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
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

        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                        finish();
                        Intent intent = new Intent(RecentActivity.this, RecentActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    // your stuff here
                };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.displaythreads_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_post:
                Intent myIntent = new Intent(this, ComposeActivity.class);
                //myIntent.putExtra("NEWSGROUP", newsgroupName);
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

            case R.id.menu_search:
                startActivity(new Intent(this, SearchActivity.class));

            case R.id.menu_mark_all_read:
                hc.markRead();
                hc.getNewest(false);
                hc.getNewsGroups();
        }
        return false;
    }

    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, SearchActivity.class));
        return true;
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