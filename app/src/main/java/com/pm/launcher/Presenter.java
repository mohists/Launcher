package com.pm.launcher;

import com.pm.launcher.mode.AppInfo;

import java.util.ArrayList;

/**
 * Created by pmcho on 2017/12/17.
 */

public class Presenter implements Callbacks {
    @Override
    public boolean setLoadOnResume() {
        return false;
    }

    @Override
    public void startBinding() {

    }

    @Override
    public void bindAllApplications(ArrayList<AppInfo> apps) {

    }

    @Override
    public void bindAppsAdded(ArrayList<AppInfo> addedApps) {

    }

    @Override
    public void bindAppsUpdated(ArrayList<AppInfo> apps) {

    }

    @Override
    public void bindAppInfosRemoved(ArrayList<AppInfo> appInfos) {

    }

    @Override
    public boolean isAllAppsButtonRank(int rank) {
        return false;
    }

    @Override
    public void onPageBoundSynchronously(int page) {

    }

    @Override
    public void dumpLogsToLocalData() {

    }
}
