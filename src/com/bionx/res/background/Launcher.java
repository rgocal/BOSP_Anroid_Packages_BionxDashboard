package com.bionx.res.background;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class Launcher {

    public String title;
    public String pkgName;
    public Drawable icon;
    public Intent intent;

    public boolean isDefault = false;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Launcher: ");
        builder.append(title);
        builder.append("; Package: ");
        builder.append(pkgName);
        builder.append("; Icon: ");
        builder.append(icon.getMinimumWidth());
        builder.append("x");
        builder.append(icon.getMinimumHeight());

        return builder.toString();
    }

}
