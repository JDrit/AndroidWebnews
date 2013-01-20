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
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * The dialog object used when the stored API is invalid.
 * This provides a settings button, which goes to settings
 * and a cancel button.
 */
public class InvalidApiKeyDialog extends AlertDialog {
	Context c;
	protected InvalidApiKeyDialog(Context context) {
		super(context);
		c = context;
		setTitle("Warning");
		setMessage("Invalid API key");
		setCancelable(true);
		setButton(BUTTON_POSITIVE, "Settings", new settingsListener());
		setButton(BUTTON_NEGATIVE, "Cancel", new cancelListener());
		
	}
	
	private final class settingsListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			c.startActivity(new Intent(c, SettingsActivity.class));
			
		}
		
	}
	
	private final class cancelListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
		}
		
	}

}
