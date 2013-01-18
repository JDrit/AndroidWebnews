package edu.rit.csh.androidwebnews;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;


import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
	/**
	 * Determines whether to always show the simplified settings UI, where
	 * settings are presented in a single list. When false, settings are shown
	 * as a master/detail two-pane view on tablets. When true, a single pane is
	 * shown on tablets.
	 */
	private static final boolean ALWAYS_SIMPLE_PREFS = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
		 addPreferencesFromResource(R.xml.preferences);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPref.registerOnSharedPreferenceChangeListener(listener);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}



	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		
		
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {		
			
			Intent intent = new Intent(SettingsActivity.this, UpdaterService.class);
			PendingIntent pintent = PendingIntent.getService(SettingsActivity.this, 0, intent, 0);
			AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			
			// if the run service is selected, an alarm is started to repeat over given time
			if (prefs.getBoolean("run_service", false)) {
				alarm.cancel(pintent);
				alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Integer.valueOf(prefs.getString("time_between_checks", "15")) * 60000, pintent);
			} else {
				alarm.cancel(pintent);
			}
		}
	};
	
	
}