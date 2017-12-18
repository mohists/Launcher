package com.pm.launcher;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.Collections;

/**
 * Created by puming on 2017/2/8.
 */

public class AppItemTouchHelper extends ItemTouchHelper {
    private static final String TAG = "AppItemTouchHelper";

    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #attachToRecyclerView(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    public AppItemTouchHelper(Callback callback) {
        super(callback);
    }

    public static class AppItemTouchHelperCallback extends Callback{
        
        private AppInfoAdapter mAdapter;
        
        public AppItemTouchHelperCallback(AppInfoAdapter adapter) {
            this.mAdapter=adapter;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            Log.d(TAG, "getMovementFlags: ");
            int dragFlag;
            int swipeFlag;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP
                        | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
                swipeFlag = 0;
            } else {
                dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
                swipeFlag = ItemTouchHelper.END;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Log.d(TAG, "onMove: ");
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mAdapter.getData(), i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mAdapter.getData(), i, i - 1);
                }
            }
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d(TAG, "onSwiped: ");
        }

        @Override
        public boolean isLongPressDragEnabled() {
            Log.d(TAG, "isLongPressDragEnabled: ");
            return super.isLongPressDragEnabled();
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            Log.d(TAG, "isItemViewSwipeEnabled: ");
            return super.isItemViewSwipeEnabled();
        }

        @Override
        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
            Log.d(TAG, "canDropOver: ");
            current.itemView.findViewById(R.id.tv_uninstall).setVisibility(View.INVISIBLE);

            return super.canDropOver(recyclerView, current, target);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

            Log.d(TAG, "onSelectedChanged: ");
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                Log.d(TAG, "onSelectedChanged: ACTION_STATE_DRAG");
                viewHolder.itemView.setAlpha(0.4f);
                viewHolder.itemView.setContentDescription("hello");
//                viewHolder.itemView.setBackgroundColor(Color.parseColor("#FF0000"));
                viewHolder.itemView.findViewById(R.id.tv_uninstall).setVisibility(View.VISIBLE);
            }
            if(actionState == ItemTouchHelper.ACTION_STATE_IDLE){
                Log.d(TAG, "onSelectedChanged: ACTION_STATE_IDLE");
            }
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                Log.d(TAG, "onSelectedChanged: ACTION_STATE_SWIPE");
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            Log.d(TAG, "clearView: ");
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1f);
        }
    }
}
