package com.bionx.res.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.bionx.res.R;
import com.bionx.res.background.LaunchersListAdapter;
import com.bionx.res.background.LaunchersManager;

public class LauncherManager extends Activity {

    private LaunchersManager mLaunchersManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_main);

        mLaunchersManager = new LaunchersManager(this);
        mLaunchersManager.updateList();

        final LaunchersListAdapter adapter = new LaunchersListAdapter(this, mLaunchersManager.getLaunchers());
        ListView launchersLV = (ListView) findViewById(R.id.launchers_lv);
        launchersLV.setAdapter(adapter);

        registerForContextMenu(launchersLV);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.launcher_menu, menu);

        menu.setHeaderTitle("Actions");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }
}
