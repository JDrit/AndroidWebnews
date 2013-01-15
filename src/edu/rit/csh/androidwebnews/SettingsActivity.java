package edu.rit.csh.androidwebnews;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Runs The settings page where user prefs, (api key, service, etc.) are stored
 */
public class SettingsActivity extends Activity {
	EditText apiText, timeBetweenEdit;
	TextView errorText;
	CheckBox checkbox;
	
	/**
	 * Created the activity and starts the settings fragment. Gets the api key
	 * to place in the text
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		apiText = (EditText) findViewById(R.id.apiEdit);
		errorText = (TextView) findViewById(R.id.settingsError);
		timeBetweenEdit = (EditText) findViewById(R.id.TimeBetweenEdit);
		checkbox = (CheckBox) findViewById(R.id.syncCheckBox);
		
		// pulls the settings data from preferences and adds them to the views
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		apiText.setText(sharedPref.getString(getString(R.string.api_key), ""));
		timeBetweenEdit.setText(Integer.valueOf(sharedPref.getInt(getString(R.string.time_between_check), 15)).toString());
		checkbox.setChecked(sharedPref.getBoolean(getString(R.string.background_sync), true));
		
	}


	/**
	 * The method called when the save button is pressed. If the api key entered
	 * is valid, it saves it to file and finishes the activity, else it tells the
	 * user to enter it in again.
	 * @param view - the Button that was pressed
	 */
	public void saveData(View view) {
		String apiKey = apiText.getText().toString();
		int timeBetween = 15;
		try {
		timeBetween = Integer.parseInt(timeBetweenEdit.getText().toString());
		} catch (Exception e) {
			
		}
		HttpsConnector hc = new HttpsConnector(apiKey, this);
		if(hc.validApiKey()) {
			SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putString(getString(R.string.api_key), apiKey);
			editor.putInt(getString(R.string.time_between_check), timeBetween);
			editor.putBoolean(getString(R.string.background_sync), checkbox.isChecked());
			editor.commit();
			finish();
		} else {
			errorText.setText("Invalid API Key, please enter a valid one");
			apiText.setText("");
		}

	}

}
