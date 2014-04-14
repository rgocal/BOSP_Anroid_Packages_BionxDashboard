package com.bionx.res.activity;

import java.util.List;

import android.preference.PreferenceActivity;
import android.preference.PreferenceActivity.Header;

import com.bionx.res.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Roms extends PreferenceActivity {
    @Override
public void onBuildHeaders(List<Header> headers) {
    loadHeadersFromResource(R.xml.roms, headers);
    
}
}