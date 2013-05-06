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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Used to start the updater service, if selected by the user's configuration, when
 * the application boots
 * @author JD
 *
 */
public class BootCompletetedIntentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) { 
			
			Intent serviceIntent = new Intent(context, UpdaterService.class);
			PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);
			AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

			// if the run service is selected, an alarm is started to repeat over given time
			if (sharedPref.getBoolean("run_service", false)) {
				String timeString= sharedPref.getString("time_between_checks", "15");
				int time = 15;
				if (!timeString.equals("")) {
					time = Integer.valueOf(timeString);
				}
				alarm.cancel(pintent);
				alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), time * 60000, pintent);
			} else {
				alarm.cancel(pintent);
			}  
		}  
		
	}

}
