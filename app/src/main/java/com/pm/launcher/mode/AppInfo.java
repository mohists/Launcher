package com.pm.launcher.mode;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.pm.launcher.compat.LauncherActivityInfoCompat;
import com.pm.launcher.compat.UserHandleCompat;
import com.pm.launcher.compat.UserManagerCompat;

/**
 * Created by puming on 2017/2/17.
 */

public class AppInfo{
    private boolean isChecked;
    private int color = Color.BLUE;
    private FolderInfo mFolder;

    /**
     * Intent extra to store the profile. Format: UserHandle
     */
    static final String EXTRA_PROFILE = "profile";

    /**
     * Title of the item
     */
    public CharSequence title;

    /**
     * Content description of the item.
     */
    public CharSequence contentDescription;

    /**
     * The intent used to start the application.
     */
    public Intent intent;

    /**
     * A bitmap version of the application icon.
     */
    public Bitmap iconBitmap;

    /**
     * Indicates whether we're using a low res icon
     */
    boolean usingLowResIcon;

    public ComponentName componentName;

    static final int DOWNLOADED_FLAG = 1;
    static final int UPDATED_SYSTEM_APP_FLAG = 2;

    int flags = 0;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public FolderInfo getFolder() {
        return mFolder;
    }

    public void setFolder(FolderInfo folder) {
        mFolder = folder;
    }

    public static Intent makeLaunchIntent(Context context, LauncherActivityInfoCompat info,
                                          UserHandleCompat user) {
        long serialNumber = UserManagerCompat.getInstance(context).getSerialNumberForUser(user);
        return new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setComponent(info.getComponentName())
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                .putExtra(EXTRA_PROFILE, serialNumber);
    }

}
