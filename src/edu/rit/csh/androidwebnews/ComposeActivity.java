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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeActivity extends Activity implements ActivityInterface {
	private String subject;
	private String body;
	private String newsgroup;
	private int parentId = -1;
	private EditText subLine;
	private EditText bodyText;
	private Spinner spinner;
	private ArrayList<String> groupNames;
	private ArrayAdapter<String> listAdapter;
	private HttpsConnector hc;
	private InvalidApiKeyDialog dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		hc = new HttpsConnector(this);	    	    
	    hc.getNewsGroups(); // used for list of newsgroups to look through
		if(extras != null)
		{
			subject = extras.getString("SUBJECT");
			body = extras.getString("QUOTED_TEXT");
			parentId = extras.getInt("PARENT");
			newsgroup = extras.getString("NEWSGROUP");
		}
		
		setContentView(R.layout.activity_compose);
		dialog = new InvalidApiKeyDialog(this);
		//TextView tv = (TextView) findViewById(R.id.compose_text);
		//tv.setText("Compose a message in " + newsgroup);
		groupNames = new ArrayList<String>();
		listAdapter = new ArrayAdapter<String>(this, R.layout.rowlayout, groupNames);
		spinner = (Spinner) findViewById(R.id.newsgroupSpinner);
		spinner.setAdapter(listAdapter);
		
		subLine = (EditText) findViewById(R.id.subject_line);
		subLine.setText(subject);
		
		bodyText = (EditText) findViewById(R.id.post_body);
		bodyText.setText(body);
		
		setTitle("Compose");
	}
	
	public void update(String jsonString) {
		JSONObject obj;
		try {
			obj = new JSONObject(jsonString);
			if (obj.has("error")) {
				if (!dialog.isShowing()) {
					dialog.show();
				}
			} else if(obj.has("newsgroups")) {
				listAdapter.clear();
				for (Newsgroup newsgroup : hc.getNewsGroupFromString(jsonString)) {
					listAdapter.add(newsgroup.name);
				}
				if (newsgroup != null) { // the composer was started from a newsgroup
					spinner.setSelection(listAdapter.getPosition(newsgroup));
				}
				listAdapter.notifyDataSetChanged();	
			}
			
		} catch (JSONException e) {
			
		}
		
	}
	
	public void abandon(View view) {
		Toast.makeText(getApplicationContext(), "Post abandoned", Toast.LENGTH_LONG).show();
		finish();
	}
	
	public void submit(View view) {

		Log.d("newpost", "Newsgroup id: " + (String)spinner.getSelectedItem());
		Log.d("newpost", "Subject: " + subLine.getText().toString());
		Log.d("newpost", "Body: " + bodyText.getText().toString());
		Log.d("newpost", "ParentId: " + parentId);
		
		if(parentId == -1)
			new HttpsConnector(this).composePost((String)spinner.getSelectedItem(), subLine.getText().toString(), bodyText.getText().toString());
		else
			new HttpsConnector(this).composePost((String)spinner.getSelectedItem(), subLine.getText().toString(), bodyText.getText().toString(), (String)spinner.getSelectedItem(), parentId);
		Toast.makeText(getApplicationContext(), "Post submitted, refresh your view!", Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public void onNewsgroupSelected(String newsgroupName) {
	
	}
}