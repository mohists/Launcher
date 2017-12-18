package com.pm.launcher;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by puming on 2016/10/18.
 */

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.ViewHolder> {
    private static final String TAG = "AppInfoAdapter";
    private Context mContext;
    private List<ResolveInfo> mResolveInfos;

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private RecyclerView mRecyclerView;
    private OnChildClickListener mOnChildClickListener;
    private OnChildLongClickListener mOnChildLongClickListener;


    public AppInfoAdapter(List<ResolveInfo> mResolveInfos, Context context) {
        this.mResolveInfos = mResolveInfos;
        this.mContext = context;

//        if(context instanceof OnChildClickListener){
//            mOnChildClickListener= (OnChildClickListener) context;
//        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public AppInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AppInfoAdapter.ViewHolder holder, int position) {
        ResolveInfo resolveInfo = mResolveInfos.get(position);
        holder.appIcon.setImageDrawable(resolveInfo.activityInfo.loadIcon(mContext.getPackageManager()));
        holder.appLabel.setText(resolveInfo.loadLabel(mContext.getPackageManager()));

        holder.appIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                int layoutPosition = holder.getLayoutPosition();
                mOnChildClickListener.onChildClick(v, layoutPosition, mResolveInfos.get(layoutPosition));
            }
        });

        holder.uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutPosition = holder.getLayoutPosition();
                mOnChildClickListener.onChildClick(v, layoutPosition, mResolveInfos.get(layoutPosition));
            }
        });

        holder.appIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: ");
                return true;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mResolveInfos == null ? 0 : mResolveInfos.size();
    }

    public List<ResolveInfo> getData() {
        if (mResolveInfos != null) {
            return mResolveInfos;
        } else {
            Log.e(TAG, "getData: mResolveInfos is null");
            return null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView appIcon;
        private TextView appLabel;
        private CardView cardView;
        private TextView uninstall;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.item);
            appIcon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
            appLabel = (TextView) itemView.findViewById(R.id.tv_app_name);
            uninstall = (TextView) itemView.findViewById(R.id.tv_uninstall);
        }
    }

    public void setOnChildClickListener(OnChildClickListener mOnChildClickListener) {
        this.mOnChildClickListener = mOnChildClickListener;
    }

    public void setOnChildLongClickListener(OnChildLongClickListener mOnChildLongClickListener) {
        this.mOnChildLongClickListener = mOnChildLongClickListener;
    }

    interface OnChildClickListener {
        void onChildClick(View view, int position, ResolveInfo resolveInfo);
    }

    interface OnChildLongClickListener {
        void onChildLongClick(View view, int position, ResolveInfo resolveInfo);
    }
}
