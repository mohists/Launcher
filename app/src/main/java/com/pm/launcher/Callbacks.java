package com.pm.launcher;

import android.content.ComponentName;
import android.util.LongSparseArray;

import com.pm.launcher.mode.AppInfo;
import com.pm.launcher.mode.FolderInfo;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by pmcho on 2017/12/17.
 */

interface Callbacks {
    boolean setLoadOnResume();

    void startBinding();


    void bindAllApplications(ArrayList<AppInfo> apps);

    void bindAppsAdded(ArrayList<AppInfo> addedApps);

    void bindAppsUpdated(ArrayList<AppInfo> apps);

    void bindAppInfosRemoved(ArrayList<AppInfo> appInfos);

    boolean isAllAppsButtonRank(int rank);

    void onPageBoundSynchronously(int page);

    void dumpLogsToLocalData();
}
