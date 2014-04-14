package com.bionx.res.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.bionx.res.R;

public class Preferences extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  addPreferencesFromResource(R.xml.preferences);
	 }
	}