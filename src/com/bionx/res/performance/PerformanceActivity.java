package com.bionx.res.performance;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import com.bionx.res.R;
import com.bionx.res.activity.ShellInterface;
import com.bionx.res.fragments.Advanced;
import com.bionx.res.fragments.CPUInfo;
import com.bionx.res.fragments.CPUSettings;
import com.bionx.res.fragments.DiskInfo;
import com.bionx.res.fragments.OOMSettings;
import com.bionx.res.fragments.TimeInState;
import com.bionx.res.fragments.VoltageControlSettings;
import com.bionx.res.util.Constants;
import com.bionx.res.util.Helpers;

public class PerformanceActivity extends FragmentActivity implements Constants {

    SharedPreferences mPreferences;
    PagerTabStrip mPagerTabStrip;
    ViewPager mViewPager;

    private static boolean mVoltageExists;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!ShellInterface.isRooted()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Superuser Error");
			builder.setMessage("SU Permissions were not found in your system and are required to continue...Are you rooted?");
			builder.setCancelable(false);
			builder.setPositiveButton("Exit",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			builder.create().show();
		} else if (!ShellInterface.hasSysfs()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Fatal Error");
			builder.setMessage("This device's kernel does not have the ability to overclock...");
			builder.setCancelable(false);
			builder.setNegativeButton("Exit",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			builder.create().show();
		} else {
        
        setContentView(R.layout.nx_performance);
        
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mVoltageExists = Helpers.voltageFileExists();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TitleAdapter titleAdapter = new TitleAdapter(getFragmentManager());
        mViewPager.setAdapter(titleAdapter);
        mViewPager.setCurrentItem(0);

        mPagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
        mPagerTabStrip.setBackgroundColor(getResources().getColor(R.color.transparent));
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.holo_blue));
        mPagerTabStrip.setDrawFullUnderline(true);

	    Helpers.shCreate();
    }
    }

    class TitleAdapter extends FragmentPagerAdapter {
        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        public TitleAdapter(FragmentManager fm) {
            super(fm);
            if (mVoltageExists) {
            	if(Helpers.showBattery()){
	                frags[0] = new CPUSettings();
	                frags[1] = new VoltageControlSettings();
	                frags[2] = new TimeInState();
	                frags[3] = new CPUInfo();
            	}
            	else{
			        frags[0] = new CPUSettings();
                	frags[1] = new VoltageControlSettings();
                	frags[2] = new TimeInState();
                	frags[3] = new CPUInfo();
            	}
            } 
            else {
                if(Helpers.showBattery()){
                    frags[0] = new CPUSettings();
                    frags[1] = new TimeInState();
                    frags[2] = new CPUInfo();
                }
                else{
                    frags[0] = new CPUSettings();
                    frags[1] = new TimeInState();
                    frags[2] = new CPUInfo();
                }
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

    private String[] getTitles() {
        String titleString[];
        if (mVoltageExists) {
        	if(Helpers.showBattery()){
                titleString = new String[]{
                        getString(R.string.t_cpu_settings),
                        getString(R.string.t_battery_info),
                        getString(R.string.t_volt_settings),
                        getString(R.string.t_time_in_state),
                        getString(R.string.t_cpu_info)};
                }
                else{
                    titleString = new String[]{
                            getString(R.string.t_cpu_settings),
                            getString(R.string.t_volt_settings),
                            getString(R.string.t_time_in_state),
                            getString(R.string.t_cpu_info)};
                }
        } 
        else {
        	if(Helpers.showBattery()){
                titleString = new String[]{
                        getString(R.string.t_cpu_settings),
                        getString(R.string.t_battery_info),
                        getString(R.string.t_time_in_state),
                        getString(R.string.t_cpu_info)};
            }
        	else{
                titleString = new String[]{
                        getString(R.string.t_cpu_settings),
                        getString(R.string.t_time_in_state),
                        getString(R.string.t_cpu_info)};
            }
        }
        return titleString;
    }
    }
}
        