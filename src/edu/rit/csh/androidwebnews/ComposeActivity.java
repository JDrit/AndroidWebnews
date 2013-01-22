package edu.rit.csh.androidwebnews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeActivity extends Activity {
	String subject;
	String body;
	String newsgroup;
	
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
		}
		
		setContentView(R.layout.activity_compose);
		
		EditText subLine = (EditText) findViewById(R.id.subject_line);
		subLine.setText(subject);
		
		EditText bodyText = (EditText) findViewById(R.id.post_body);
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
		//TODO submit post
		Toast.makeText(getApplicationContext(), "Post submitted", Toast.LENGTH_LONG).show();
		finish();
		
	}
}
