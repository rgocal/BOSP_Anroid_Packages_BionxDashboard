package com.bionx.res.fragments;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

import com.bionx.res.R;
import com.bionx.res.background.RootUtils;
import com.bionx.res.helpers.SystemHelper;

public class SystemSettings extends PreferenceFragment {
	
	private EditTextPreference mDensity;
	private String mCurrentDPI = "";
	private EditTextPreference mNavbar;
	private String mCurrentNavbar = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Resources res = this.getResources();
		getActivity().getContentResolver();

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.performance_settings);
		//Navbar
		mNavbar = (EditTextPreference)findPreference(res.getString(R.string.navbar_prefs_key));
		try {
			mCurrentNavbar = RootUtils.executeWithResult("cat /system/build.prop | busybox grep \"qemu.hw.mainkeys\" | sed 's/qemu.hw.mainkeys=//g'\n");
			mNavbar.setPersistent(false);
			mNavbar.setText(mCurrentNavbar);
			mNavbar.setDefaultValue(mCurrentNavbar);
		} catch (IOException e) {;
		e.printStackTrace();
		}
		mNavbar.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String newNavbar = (String) newValue;
				if (newNavbar.equals(mCurrentNavbar))
					return true;
				try {
					int NavbarNumber = Integer.parseInt(newNavbar);
					if (NavbarNumber < 0 || NavbarNumber > 1)
						return false;
				} catch (NumberFormatException nfe) {
					return false;
				}
				SystemHelper.mountSystemRW();
				try {
					RootUtils.execute(String.format("busybox sed -i 's/qemu.hw.mainkeys=%s/qemu.hw.mainkeys=%s/g' /system/build.prop\n",
							mCurrentNavbar, newNavbar));
					String result = RootUtils.executeWithResult("cat /system/build.prop | busybox grep \"qemu.hw.mainkeys\"\n");
					if (result.contains(newNavbar)) {
						mCurrentNavbar = newNavbar;
						suggestReboot();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SystemHelper.mountSystemRO();
				return true;
			}
		});
		
        //Screen Desnsity
        mDensity = (EditTextPreference)findPreference(res.getString(R.string.lcd_density_prefs_key));
        try {
			mCurrentDPI = RootUtils.executeWithResult("cat /system/build.prop | busybox grep \"ro.sf.lcd_density\" | sed 's/ro.sf.lcd_density=//g'\n");
			mDensity.setPersistent(false);
			mDensity.setText(mCurrentDPI);
			mDensity.setDefaultValue(mCurrentDPI);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        mDensity.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String newDpi = (String) newValue;
				if (newDpi.equals(mCurrentDPI))
					return true;
				try {
					int dpiNumber = Integer.parseInt(newDpi);
					if (dpiNumber < 100 || dpiNumber > 480)
						return false;
				} catch (NumberFormatException nfe) {
					return false;
				}
				SystemHelper.mountSystemRW();
				try {
					Thread.sleep(250);
					RootUtils.execute(String.format("busybox sed -i 's/ro.sf.lcd_density=%s/ro.sf.lcd_density=%s/g' /system/build.prop\n",
							mCurrentDPI, newDpi));
					String result = RootUtils.executeWithResult("cat /system/build.prop | busybox grep \"ro.sf.lcd_density\"\n");
					if (result.contains(newDpi)) {
						mCurrentDPI = newDpi;
						suggestReboot();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SystemHelper.mountSystemRO();
				return true;
			}
		});

	}

	public void suggestReboot() {
		new AlertDialog.Builder(getActivity())
		.setMessage("In order for changes to take effect you must reboot your device.\n" +
				"Would you like to reboot now?")
		.setTitle("Reboot?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				RootUtils.reboot();
				dialog.dismiss();
			}
			
		})
		.setNeutralButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.show();
	}
}
