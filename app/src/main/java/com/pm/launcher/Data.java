package com.pm.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.pm.launcher.mode.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by puming on 2017/2/16.
 */

public class Data {
    public static List<ResolveInfo> getResolveInfos(Context context){
        PackageManager mPackageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mResolveInfos = mPackageManager.queryIntentActivities(intent, 0);
        if(mResolveInfos!=null&&mResolveInfos.size()>0){
          return mResolveInfos;
        }else {
            new Exception("mResolveInfos==null or mResolveInfos.size()==0");
            return null;
        }
    }

    public static List<AppInfo> getAppList(Context context){
        List<AppInfo> appInfos=new ArrayList<>();
        for (ResolveInfo resolveInfo :getResolveInfos(context)) {
            appInfos.add(new AppInfo());
        }
        return appInfos;
    }
}
