package com.pm.launcher.back;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.pm.launcher.R;

import java.util.List;

/**
 * Created by puming on 2016/10/17.
 */

public class AppIconAdapter extends CommonAdapter<ResolveInfo> {
    private static final String TAG = "AppIconAdapter";
    private Context mContext;

    public AppIconAdapter(Context context, List<ResolveInfo> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mContext=context;
        Log.d(TAG, "AppIconAdapter: datas.size="+datas.size());
    }

    @Override
    public void convert(ViewHolder holder, ResolveInfo resolveInfo) {
        holder.setImagView(R.id.iv_app_icon,resolveInfo.activityInfo.loadIcon(mContext.getPackageManager()));
        holder.setText(R.id.tv_app_name,(String) resolveInfo.loadLabel(mContext.getPackageManager()));
    }
}
