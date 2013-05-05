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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeActivity extends Activity {
	String subject;
	String body;
	String newsgroup;
	int parentId = -1;
	EditText subLine;
	EditText bodyText;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null)
		{
			subject = extras.getString("SUBJECT");
			body = extras.getString("QUOTED_TEXT");
			newsgroup = extras.getString("NEWSGROUP");
			parentId = extras.getInt("PARENT");
		}
		
		setContentView(R.layout.activity_compose);
		
		TextView tv = (TextView) findViewById(R.id.compose_text);
		tv.setText("Compose a message in " + newsgroup);
		
		subLine = (EditText) findViewById(R.id.subject_line);
		subLine.setText(subject);
		
		bodyText = (EditText) findViewById(R.id.post_body);
		bodyText.setText(body);
		
		setTitle("Compose");
	}
	
	public void abandon(View view)
	{
		Toast.makeText(getApplicationContext(), "Post abandoned", Toast.LENGTH_LONG).show();
		finish();
	}
	
	public void submit(View view)
	{

		Log.d("newpost", "Newsgroup id: " + newsgroup);
		Log.d("newpost", "Subject: " + subLine.getText().toString());
		Log.d("newpost", "Body: " + bodyText.getText().toString());
		Log.d("newpost", "ParentId: " + parentId);
		
		if(parentId == 0)
			new HttpsConnector(this).composePost(newsgroup, subLine.getText().toString(), bodyText.getText().toString());
		else
			new HttpsConnector(this).composePost(newsgroup, subLine.getText().toString(), bodyText.getText().toString(), newsgroup, parentId);
		Toast.makeText(getApplicationContext(), "Post submitted, refresh your view!", Toast.LENGTH_LONG).show();
		finish();
	}
}