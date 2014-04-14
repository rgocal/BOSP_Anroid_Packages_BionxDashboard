package com.bionx.res.background;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bionx.res.R;

public class LaunchersListAdapter extends BaseAdapter implements View.OnClickListener {

    private final List<Launcher> mLaunchers;
    private final LayoutInflater mInflater;
    private final Context mContext;

    public LaunchersListAdapter(Context context, List<Launcher> launchers) {
        mContext = context;
        mLaunchers = launchers;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mLaunchers.size();
    }

    @Override
    public Object getItem(int i) {
        return mLaunchers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.list_launcher, null);

            viewHolder.iconIV = (ImageView) view.findViewById(R.id.icon_iv);
            final LinearLayout ll = (LinearLayout) view.findViewById(R.id.info_ll);
            viewHolder.titleTV = (TextView) ll.findViewById(R.id.title);
            viewHolder.pkgNameTV = (TextView) ll.findViewById(R.id.pkg_name);
            viewHolder.uninstallIV = (ImageView) view.findViewById(R.id.uninstall_iv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Launcher launcher = mLaunchers.get(i);

        final String title = (launcher.isDefault) ? (launcher.title + " [default]") : launcher.title;
        viewHolder.iconIV.setImageDrawable(launcher.icon);
        viewHolder.titleTV.setText(title);
        viewHolder.pkgNameTV.setText(launcher.pkgName);
        viewHolder.launcher = launcher;

        viewHolder.uninstallIV.setOnClickListener(this);
        viewHolder.uninstallIV.setTag(launcher.pkgName);

        view.setOnClickListener(this);
        viewHolder.iconIV.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list_rl:
                final ViewHolder viewHolder = (ViewHolder) view.getTag();
                mContext.startActivity(viewHolder.launcher.intent);
                break;

            case R.id.uninstall_iv:
                final String pkgName = (String) view.getTag();
                uninstallApp(pkgName);

                break;
        }
    }


    private void uninstallApp(String pkgName) {
        final Uri uri = Uri.fromParts("package", pkgName, null);
        final Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        mContext.startActivity(intent);
    }

    /**
     * ViewHolder
     */
    static class ViewHolder {
        public ImageView iconIV;
        public TextView titleTV;
        public TextView pkgNameTV;
        public ImageView uninstallIV;

        public Launcher launcher;
    }

}
