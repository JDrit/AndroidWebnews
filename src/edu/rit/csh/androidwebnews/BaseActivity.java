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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

/**
 * This is an abstract class that is used to keep track of if the activity is in the foreground
 * or not. If it is not, then the activity will not update so it does not throw exceptions with
 * the fragments
 * @author JD
 */
public abstract class BaseActivity extends SherlockFragmentActivity {
    protected SharedPreferences sharedPref;
    protected HttpsConnector hc;
    protected ActionBar actionBar;

    /**
     * This is used to determine if the activity is currently in the foreground. This is needed so
     * activities in the background will not update
     */
    private boolean active;

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
        actionBar = getSupportActionBar();
        if (!(this instanceof RecentActivity)) {
            actionBar.setHomeButtonEnabled(true);
        }

        hc = new HttpsConnector(this);
        active = true;
    }

    @Override
    public void onResume() {
        active = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        active = false;
        super.onResume();
    }

    /**
     * Called when there is data to update the activity. The activity is only updated if the activity
     * is currently in the foreground
     * @param jsonString the data to update
     */
    public void shouldUpdate(String jsonString) {
        if (active) {
            update(jsonString);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, RecentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Used to make sure that the Activities have a method that can be called
     * when the async task finishes and needs to update the Activity / Fragment.
     * This should then call the update method in the fragment
     *
     * @param jsonString - the output of the async task
     */
    protected abstract void update(String jsonString);

    public abstract void onNewsgroupSelected(final String newsgroupName);
}
