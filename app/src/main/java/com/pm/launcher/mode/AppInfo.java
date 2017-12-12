package com.pm.launcher.mode;

import android.graphics.Color;

/**
 * Created by puming on 2017/2/17.
 */

public class AppInfo extends ItemInfo {
    private boolean isChecked;
    private int color = Color.BLUE;
    private FolderInfo mFolder;

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
}
