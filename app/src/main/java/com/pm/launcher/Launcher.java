package com.pm.launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.List;

public class Launcher extends Activity implements AppInfoAdapter.OnChildClickListener {

    private GridView mGridView;
    private RelativeLayout laucher;

    private RecyclerView mRecyclerView;

    private PackageManager mPackageManager;
    private List<ResolveInfo> mResolveInfos;
    private AppInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        initialize();
        initData();
//        AppIconAdapter adapter=new AppIconAdapter(this,mResolveInfos,R.layout.item_app);
//        mGridView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new AppInfoAdapter(mResolveInfos, this);
        mRecyclerView.setAdapter(adapter);
        AppItemTouchHelper.AppItemTouchHelperCallback touchHelperCallback = new AppItemTouchHelper.AppItemTouchHelperCallback(adapter);
        AppItemTouchHelper itemTouchHelper = new AppItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        registerListener();
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
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_container);
//        mGridView = (GridView) findViewById(R.id.gv_container);
        laucher = (RelativeLayout) findViewById(R.id.laucher);
    }

    @Override
    public void onChildClick(View view, int position, ResolveInfo resolveInfo) {
        startActivity(position);
    }
}
