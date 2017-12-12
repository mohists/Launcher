package com.pm.launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.pm.launcher.widget.LauncherView;

import java.util.List;

public class Launcher extends Activity implements AppInfoAdapter.OnChildClickListener {

    private static final String TAG = "Launcher";
    private RelativeLayout laucher;

    private RecyclerView mRecyclerView;
    private LauncherView mLauncherView;
    private PackageManager mPackageManager;
    private List<ResolveInfo> mResolveInfos;
    private AppInfoAdapter adapter;
    private AppListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        initialize();
//        initData();
        /*GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new AppInfoAdapter(mResolveInfos, this);
        mRecyclerView.setAdapter(adapter);
        AppItemTouchHelper.AppItemTouchHelperCallback touchHelperCallback = new AppItemTouchHelper.AppItemTouchHelperCallback(adapter);
        AppItemTouchHelper itemTouchHelper = new AppItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        registerListener();*/
        //
//        mLauncherView.setAdapter(new AppListAdapter(Data.getResolveInfos(this),this));
        mAdapter = new AppListAdapter();
        mAdapter.registerObserver(new AppListAdapter.MyObserver() {
            @Override
            public void onChecked(boolean isChecked) {
                super.onChecked(isChecked);
            }

            @Override
            public void onEditChanged(boolean inEdit) {
                super.onEditChanged(inEdit);
            }

            @Override
            public void onHideSubDialog() {
                super.onHideSubDialog();
            }

            @Override
            public void onRestore() {
                super.onRestore();
            }
        });
        mAdapter.addData(Data.getAppList(this));
        mLauncherView.setAdapter(mAdapter);
        mLauncherView.setDebugAble(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent: ");
        mAdapter.setEditMode(false);
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        mAdapter.setEditMode(false);
        super.onBackPressed();
    }

    private void registerListener() {
       /* mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(position);
            }
        });*/

        adapter.setOnChildClickListener(new AppInfoAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(View view, int position, ResolveInfo resolveInfo) {
                startActivity(position);
            }
        });
    }

    private void startActivity(int position) {
        ResolveInfo info = mResolveInfos.get(position);
        String packageName = info.activityInfo.packageName;
        String name = info.activityInfo.name;
        ComponentName componentName = new ComponentName(packageName, name);
        Intent intent = new Intent();
        intent.setComponent(componentName);
        startActivity(intent);
    }

    private void initData() {
        mPackageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        mResolveInfos = mPackageManager.queryIntentActivities(intent, 0);
    }

    private void initialize() {
        mLauncherView = (LauncherView) findViewById(R.id.launcher_view);
//        mRecyclerView = (RecyclerView) findViewById(R.id.rv_container);
        laucher = (RelativeLayout) findViewById(R.id.laucher);
    }

    @Override
    public void onChildClick(View view, int position, ResolveInfo resolveInfo) {
        startActivity(position);
    }
}
