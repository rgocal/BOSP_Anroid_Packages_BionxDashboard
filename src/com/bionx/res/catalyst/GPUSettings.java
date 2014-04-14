package com.bionx.res.catalyst;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.TwoStatePreference;
import android.util.Log;
import android.widget.Toast;

import com.bionx.res.R;
import com.bionx.res.util.GPUProcessor;

public class GPUSettings extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener {

	public SharedPreferences prefs;
	public ListPreference maxgpu;
	public ListPreference mingpu;
	public CheckBoxPreference gpuboot;
	public String maxValue, minValue, boot;
	public TwoStatePreference applygpu;


	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.gpu_preferences);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		maxgpu = (ListPreference) findPreference("maxgpu");
		maxgpu.setOnPreferenceChangeListener(this);
		mingpu = (ListPreference) findPreference("mingpu");
		mingpu.setOnPreferenceChangeListener(this);
		gpuboot = (CheckBoxPreference) findPreference("gpuboot");
		gpuboot.setOnPreferenceClickListener(this);
		applygpu = (TwoStatePreference) findPreference("applygpu");
		applygpu.setOnPreferenceClickListener(this);
		
		String mValue = prefs.getString("maxgpu", "Select max frequency");
		maxgpu.setSummary(mValue);
		String mIValue = prefs.getString("mingpu", "Select min frequency");
		mingpu.setSummary(mIValue);
		
		maxValue = prefs.getString("maxgpu", "416");
		minValue = prefs.getString("mingpu", "157");

	}


	@Override
	public boolean onPreferenceChange(Preference pref, Object newvalue) {
		// TODO Auto-generated method stub
		if(pref == maxgpu) {
			maxValue = (String)newvalue;
			Log.d("GPU Overclock", "================"+maxValue+"============");
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("maxgpu", maxValue);
			editor.commit();
			maxgpu.setSummary(maxValue);

		}
		if(pref == mingpu) {
			minValue = (String)newvalue;
			Log.d("GPU Overclock", "================"+minValue+"============");
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("mingpu", minValue);
			editor.commit();
			mingpu.setSummary(minValue);
		}

		return false;
	}


	@Override
	public boolean onPreferenceClick(Preference pref) {
		// TODO Auto-generated method stub
		if (pref == applygpu) {
			boolean value = applygpu.isChecked();
			if(value == true) {
				if(maxValue.equals("416")) {
					GPUProcessor.runSuCommand("busybox echo '" + maxValue + " " +maxValue + " "+maxValue + " "+maxValue + " " + maxValue + " " + "384 307" + minValue +"'" +" > sys/devices/system/cpu/cpu0/cpufreq/gpu_oc ");
					Toast.makeText(this, "frequencies applied:" + "\n Max: "  + maxValue + "\n Min: " + minValue, Toast.LENGTH_LONG).show();

				}
				else {
					GPUProcessor.runSuCommand("busybox echo '" + maxValue + " " +maxValue + " "+maxValue + " "+maxValue + " "+ "416 384 307 157 " + minValue +"'" +" > sys/devices/system/cpu/cpu0/cpufreq/gpu_oc ");
					Toast.makeText(this, "frequencies applied:" + "\n Max: "  + maxValue + "\n Min: " + minValue, Toast.LENGTH_SHORT).show();
				};
			}
			if(value == false) {
				GPUProcessor.runSuCommand("busybox echo '416 416 416 416 384 307 157' > sys/devices/system/cpu/cpu0/cpufreq/gpu_oc ");
				Toast.makeText(this, "stock values applied:" + "\n Max: 416\n Min: 157", Toast.LENGTH_SHORT).show();
			}
		}

		if(pref == gpuboot) {
			boolean value = gpuboot.isChecked();
			if(value == true) {
				if(maxValue.equals("416")) {
					boot = "echo " + maxValue + " " +maxValue + " "+maxValue + " "+maxValue + " " + maxValue + " " + "384 307 " + minValue + " " + "> sys/devices/system/cpu/cpu0/cpufreq/gpu_oc";
				}
				else {
					boot = "echo " +  maxValue + " " +maxValue + " "+maxValue + " "+maxValue + " "+ "416 384 307 157 " + minValue + " " + "> sys/devices/system/cpu/cpu0/cpufreq/gpu_oc";
				}
				GPUProcessor.runSuCommand("busybox mount -o remount,rw /system");
				GPUProcessor.runSuCommand("busybox echo '#!/system/bin/sh' > system/etc/init.d/99bootgpu");
				GPUProcessor.runSuCommand("busybox echo 'sleep 60' >> system/etc/init.d/99bootgpu");
				GPUProcessor.runSuCommand("busybox echo '" + boot + "' >> system/etc/init.d/99bootgpu");
				GPUProcessor.runSuCommand("busybox chmod 755 /system/etc/init.d/99bootgpu");
				GPUProcessor.runSuCommand("busybox mount -o remount,ro /system");
			}
			if (value == false) {
				GPUProcessor.runSuCommand("busybox mount -o remount,rw /system");
				GPUProcessor.runSuCommand("busybox rm -f /system/etc/init.d/99bootgpu");
				GPUProcessor.runSuCommand("busybox mount -o remount,ro /system");
			}
		}

		return false;
	}
}