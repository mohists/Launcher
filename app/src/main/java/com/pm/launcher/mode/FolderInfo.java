package com.pm.launcher.mode;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by puming on 2017/2/17.
 */

public class FolderInfo extends AppInfo {
    private List<AppInfo> mChild = new ArrayList<>();
    private String mCategory;

    public void addChild(@NonNull AppInfo AppInfo){
        AppInfo.setFolder(this);
        mChild.add(AppInfo);
    }

    public void addChild(int location,@NonNull AppInfo AppInfo){
        AppInfo.setFolder(this);
        mChild.add(location,AppInfo);
    }

    public AppInfo removeChild(int location){
        AppInfo appInfo = mChild.remove(location);
        appInfo.setFolder(null);
        return appInfo;
    }

    public boolean removeChild(@NonNull AppInfo AppInfo){
        AppInfo.setFolder(null);
        return mChild.remove(AppInfo);
    }


    public int getChildCount(){
        return mChild.size();
    }


    public AppInfo getChild(int position){
        return mChild.get(position);
    }


    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public int getCheckedCount(){
        if(mChild != null){
            int i = 0;
            for(AppInfo data:mChild){
                if(data.isChecked()){
                    i++;
                }
            }
            return i;
        }
        return 0;
    }
}
