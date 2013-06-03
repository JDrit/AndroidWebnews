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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * The dialog that pops up the first time the application is run.
 * This is meant to explain how to use the application.
 */
public class FirstTimeDialog extends AlertDialog {
	Context c;
	
	protected FirstTimeDialog(Context context) {
		super(context);
		
		c = context;
		setTitle("CSH News - BETA");
		setMessage("Welcome to CSH News\n" +
				"To view newsgroups, swipe from the left to open the menu. To start, enter in your API key into settings. You can find your API key in the settings tab of WebNews website");
		setCancelable(true);
		setButton(BUTTON_POSITIVE, "Settings", new okListener());
		
	}
	
	private final class okListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {

			//dialog.cancel();
			c.startActivity(new Intent(c, SettingsActivity.class));
		}
		
	}
	
	
}
