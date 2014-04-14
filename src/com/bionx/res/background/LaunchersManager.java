package com.bionx.res.background;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class LaunchersManager {

    private static final String TAG = "LauncherManager";

    private final Context mContext;
    private final PackageManager mPackageManager;
    private final Intent mLauncherIntent;

    private List<ResolveInfo> mLaunchersActivities;

    private final List<Launcher> mLaunchers = new ArrayList<Launcher>();

    public List<Launcher> getLaunchers() {
        return mLaunchers;
    }

    /**
     * Constructor
     *
     * @param context Context
     */
    public LaunchersManager(Context context) {
        mContext = context;

        mPackageManager = mContext.getPackageManager();
        mLauncherIntent = new Intent("android.intent.action.MAIN");
        mLauncherIntent.addCategory("android.intent.category.HOME");
    }

    /**
     * Update list of the launchers
     *
     * @return True if successful, false - otherwise
     */
    public boolean updateList() {
        mLaunchersActivities = mPackageManager.queryIntentActivities(mLauncherIntent, 0);
        if (mLaunchersActivities == null) {
            return false;
        }

        mLaunchers.clear();

        for (ResolveInfo launcherInfo : mLaunchersActivities) {
            final Launcher launcher = new Launcher();
            launcher.pkgName = launcherInfo.activityInfo.packageName;

            try {
                final ApplicationInfo info = mPackageManager.getApplicationInfo(launcher.pkgName, 0);
                launcher.title = mPackageManager.getApplicationLabel(info).toString();
                launcher.icon = mPackageManager.getApplicationIcon(info);
                launcher.intent = getIntentForPackage(launcher.pkgName);
                launcher.isDefault = isDefaultLauncher(launcher.pkgName);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Package not found: " + e.toString());
                continue;
            }

            mLaunchers.add(launcher);

            Log.d(TAG, launcher.toString());
        }

        return true;
    }


    /**
     * Check if the given launcher is default
     */
    private boolean isDefaultLauncher(String pkgName) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        final List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);

        final List<ComponentName> activities = new ArrayList<ComponentName>();

        mPackageManager.getPreferredActivities(filters, activities, null);

        for (ComponentName activity : activities) {
            if (pkgName.equals(activity.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Get intent for package
     *
     * @param pkgName Package name to resolve
     * @return Resolved info or null
     */
    private Intent getIntentForPackage(String pkgName) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setPackage(pkgName);

        final ResolveInfo resolveInfo = mPackageManager.resolveActivity(intent, 0);
        if (resolveInfo == null) {
            return null;
        }

        intent.setClassName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name);

        return intent;
    }

}
