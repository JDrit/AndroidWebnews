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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.ArrayList;

public class ComposeActivity extends SherlockFragmentActivity implements ActivityInterface {
    private String subject;
    private String body;
    private String newsgroup;
    private int parentId = -1;
    private EditText subLine;
    private EditText bodyText;
    private Spinner spinner;
    private ArrayAdapter<String> listAdapter;
    private HttpsConnector hc;
    private InvalidApiKeyDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String layout = sharedPref.getString("layout_pick", "default");
        if (layout.equals("default")) {
            setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
        } else if (layout.equals("dark")) {
            setTheme(R.style.Theme_Sherlock);
        } else {
            setTheme(R.style.Theme_Sherlock_Light);
        }

        Bundle extras = getIntent().getExtras();
        hc = new HttpsConnector(this);
        hc.getNewsGroups(); // used for list of newsgroups to look through

        if (extras != null) {
            if (extras.containsKey("SUBJECT")) { // coming from a reply
                subject = extras.getString("SUBJECT");
                body = extras.getString("QUOTED_TEXT");
                parentId = extras.getInt("PARENT");
            }
            // for when a new post is done from a newsgroup
            newsgroup = extras.getString("NEWSGROUP");
        }

        setContentView(R.layout.activity_compose);
        dialog = new InvalidApiKeyDialog(this);

        ArrayList<String> groupNames = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this, R.layout.rowlayout, groupNames);
        spinner = (Spinner) findViewById(R.id.newsgroupSpinner);
        if (parentId <= 0) {// do not display the spinner if it is a reply to a post
            spinner.setAdapter(listAdapter);
            spinner.setSelection(listAdapter.getPosition(newsgroup));
        } else {
            spinner.setVisibility(View.GONE);
        }

        subLine = (EditText) findViewById(R.id.subject_line);
        subLine.setText(subject);

        bodyText = (EditText) findViewById(R.id.post_body);
        bodyText.setText(body);

        setTitle("Compose");
    }

    public void update(String jsonString) {
        JSONObject obj;
        if (jsonString.startsWith("Error:")) { // error in the Async Task
            ConnectionExceptionDialog dialog = new ConnectionExceptionDialog(this, jsonString);
            dialog.show();
        } else try {
            obj = new JSONObject(jsonString);
            if (obj.has("error")) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            } else if (obj.has("newsgroups")) {
                listAdapter.clear();
                for (Newsgroup newsgroup : hc.getNewsGroupFromString(jsonString)) {
                    listAdapter.add(newsgroup.getName());
                }
                if (newsgroup != null) { // the composer was started from a newsgroup
                    spinner.setSelection(listAdapter.getPosition(newsgroup));
                }
                listAdapter.notifyDataSetChanged();
            }

        } catch (JSONException ignored) {
        }

    }

    public void abandon(View view) {
        Toast.makeText(getApplicationContext(), "Post abandoned", Toast.LENGTH_LONG).show();
        finish();
    }

    public void submit(View view) {

        if (parentId <= 0) {
            new HttpsConnector(this).composePost((String) spinner.getSelectedItem(), subLine.getText().toString(), bodyText.getText().toString());
        } else {
            new HttpsConnector(this).composePost(newsgroup, subLine.getText().toString(), bodyText.getText().toString(), newsgroup, parentId);
        }
        Toast.makeText(getApplicationContext(), "Post submitted, refresh your view!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void plus1(View view) {
        bodyText.setText(bodyText.getText() + "+1");
    }

    @Override
    public void onNewsgroupSelected(String newsgroupName) {

    }
}