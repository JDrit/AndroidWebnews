package edu.rit.csh.androidwebnews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

/**
 * The dialog object used when the stored API is invalid.
 * This provides a settings button, which goes to settings
 * and a cancel button.
 */
public class NoInternetDialog extends AlertDialog {
	Context c;
	protected NoInternetDialog(Context context) {
		super(context);
		c = context;
		setTitle("No Internet");
		setMessage("Please connect your device to the internet");
		setCancelable(true);
		setButton(BUTTON_POSITIVE, "Internet Settings", new settingsListener());
		setButton(BUTTON_NEGATIVE, "Cancel", new cancelListener());
		
	}
	
	private final class settingsListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
			c.startActivity(intent);
			//c.startActivity(new Intent(c, SettingsActivity.class));
			
		}
		
	}
	
	private final class cancelListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
		}
		
	}

}
