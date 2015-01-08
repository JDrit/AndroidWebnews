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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PostSwipeableActivity extends BaseActivity {
    private InvalidApiKeyDialog dialog;
    private static String newsgroupName;
    static int id;
    private PostPagerAdapter ppa;
    private PostThread rootThread;
    private boolean fromSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager mViewPager;
        setContentView(R.layout.activity_post_swipable);
        hc = new HttpsConnector(this);

        Bundle extras = getIntent().getExtras();
        newsgroupName = extras.getString("SELECTED_NEWSGROUP");
        id = extras.getInt("SELECTED_ID");
        int selected_id = extras.getInt("GOTO_THIS");
        fromSearch = extras.getBoolean("SEARCH_RESULTS");

        ppa = new PostPagerAdapter(getSupportFragmentManager(), fromSearch);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(ppa);
        dialog = new InvalidApiKeyDialog(this);
        mViewPager.setCurrentItem(selected_id);

        if (!fromSearch) {
            for (int x = 0; x < DisplayThreadsActivity.lastFetchedThreads.size(); x++) {
                if (DisplayThreadsActivity.lastFetchedThreads.get(x).getNumber() == id) {
                    rootThread = DisplayThreadsActivity.lastFetchedThreads.get(x);
                    break;
                }
            }
        }
    }

    public void markUnread(View view) {
        int threadId = Integer.parseInt((String) view.getTag());
        findThisThread(rootThread, threadId).setUnread("manual");
        hc.markUnread(newsgroupName, threadId);
        Toast.makeText(getApplicationContext(), "Marking post as unread", Toast.LENGTH_LONG).show();
    }

    public void markStarred(View view) {
        int threadId = Integer.parseInt((String) view.getTag());

        PostThread thread = findThisThread(rootThread, threadId);
        thread.starred();
        ImageButton ib = (ImageButton) view;
        if (thread.getStarred()) {
            if (PreferenceManager.getDefaultSharedPreferences(this).getString("layout_pick", "default").equals("dark")) {
                ib.setImageResource(R.drawable.webnews_star_dark);
            } else {
                ib.setImageResource(R.drawable.webnews_star_light);
            }
            Toast.makeText(getApplicationContext(), "Post starred", Toast.LENGTH_LONG).show();
        } else {
            if (PreferenceManager.getDefaultSharedPreferences(this).getString("layout_pick", "default").equals("dark")) {
                ib.setImageResource(R.drawable.webnews_unstar_dark);
            } else {
                ib.setImageResource(R.drawable.webnews_unstar_light);
            }
            Toast.makeText(getApplicationContext(), "Post un-starred", Toast.LENGTH_LONG).show();
        }
        hc.markStarred(newsgroupName, threadId);
    }

    private PostThread findThisThread(PostThread thread, int id) {
        if (fromSearch) {
            for (PostThread p : SearchResultsActivity.threads) {
                if (p.getNumber() == id) {
                    return p;
                }
            }
        } else {
            if (thread.getNumber() == id)
                return thread;
            else
                for (PostThread t : thread.getChildren()) {
                    PostThread returnValue = findThisThread(t, id);
                    if (returnValue != null)
                        return returnValue;
                }
        }
        return null;
    }

    public void postReply(View view) {
        String threadInfo = (String) view.getTag();
        int threadId = Integer.parseInt(threadInfo.substring(0, threadInfo.indexOf("|")));
        PostThread thread = findThisThread(rootThread, threadId);
        String subject = thread.getSubject();
        String body = threadInfo.substring(threadInfo.indexOf("|") + 1, threadInfo.length());

        String newBody = "";
        String[] lines = body.split("\n");
        for (String line : lines) newBody += ">" + line + "\n";

        newBody = "On " + thread.getDate() + ", " + thread.getAuthorName() + " wrote:\n" + newBody;

        if (!subject.substring(0, 3).equals("Re:"))
            subject = "Re: " + subject;

        Intent myIntent = new Intent(this, ComposeActivity.class);
        myIntent.putExtra("NEWSGROUP", newsgroupName);
        myIntent.putExtra("SUBJECT", subject);
        myIntent.putExtra("QUOTED_TEXT", newBody);
        myIntent.putExtra("PARENT", thread.getNumber());
        startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.postswipable_menu, menu);
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
                Intent intent = new Intent(PostSwipeableActivity.this, SearchResultsActivity.class);
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
        try {
            JSONObject obj = new JSONObject(jsonString);
            if (obj.has("error")) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            } else {
                ppa.update(jsonString);
            }
        } catch (JSONException ignored) {

        }

    }

    @Override
    public void onNewsgroupSelected(String newsgroupName) {
        //intentionally blank
    }

}