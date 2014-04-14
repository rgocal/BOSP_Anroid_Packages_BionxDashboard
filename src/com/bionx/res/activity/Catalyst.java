package com.bionx.res.activity;

import java.util.List;

import android.preference.PreferenceActivity;

import com.bionx.res.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Catalyst extends PreferenceActivity {
	    @Override
    public void onBuildHeaders(List<Header> headers) {
        loadHeadersFromResource(R.xml.catalyst, headers);
        
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slide_menu);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
