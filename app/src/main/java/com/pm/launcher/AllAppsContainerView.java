package com.pm.launcher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.method.TextKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pm.launcher.mode.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmcho on 2017/12/17.
 */

public class AllAppsContainerView extends FrameLayout implements View.OnTouchListener, View.OnLongClickListener{

    private static final int MIN_ROWS_IN_MERGED_SECTION_PHONE = 3;
    private static final int MAX_NUM_MERGES_PHONE = 2;

    private final Launcher mLauncher;
    //    private final AlphabeticalAppsList mApps;
    private  AppInfoAdapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutManager;
    private  RecyclerView.ItemDecoration mItemDecoration;

    // The computed bounds of the container
    private final Rect mContentBounds = new Rect();

    private RecyclerView mAppsRecyclerView;

    private View mSearchContainer;

    private SpannableStringBuilder mSearchQueryBuilder = null;

    private int mSectionNamesMargin;
    private int mNumAppsPerRow;
    private int mNumPredictedAppsPerRow;
    private int mRecyclerViewTopBottomPadding;
    // This coordinate is relative to this container view
    private final Point mBoundsCheckLastTouchDownPos = new Point(-1, -1);
    // This coordinate is relative to its parent
    private final Point mIconLastTouchPos = new Point();
    private View mContent;

    public AllAppsContainerView(Context context) {
        this(context, null);
    }

    public AllAppsContainerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AllAppsContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources res = context.getResources();

        mLauncher = (Launcher) context;
//        mApps = new AlphabeticalAppsList(context);
//        mAdapter = new AllAppsGridAdapter(mLauncher, mApps, this, mLauncher, this);
//        mApps.setAdapter(mAdapter);
//        mLayoutManager = mAdapter.getLayoutManager();
//        mItemDecoration = mAdapter.getItemDecoration();

        mSearchQueryBuilder = new SpannableStringBuilder();
        Selection.setSelection(mSearchQueryBuilder, 0);
    }

    /**
     * Sets the current set of predicted apps.
     */
   /* public void setPredictedApps(List<ComponentKey> apps) {
        mApps.setPredictedApps(apps);
    }*/

    /**
     * Sets the current set of apps.
     */
    public void setApps(List<AppInfo> apps) {
//        mApps.setApps(apps);
    }

    /**
     * Adds new apps to the list.
     */
    public void addApps(List<AppInfo> apps) {
//        mApps.addApps(apps);
    }

    /**
     * Updates existing apps in the list
     */
    public void updateApps(List<AppInfo> apps) {
//        mApps.updateApps(apps);
    }

    /**
     * Removes some apps from the list.
     */
    public void removeApps(List<AppInfo> apps) {
//        mApps.removeApps(apps);
    }

    /**
     * Scrolls this list view to the top.
     */
    public void scrollToTop() {
//        mAppsRecyclerView.scrollToTop();
    }


    /**
     * Resets the state of AllApps.
     */
    public void reset() {
        // Reset the search bar and base recycler view after transitioning home
//        mAppsRecyclerView.reset();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = findViewById(R.id.apps_view);


        // This is a focus listener that proxies focus from a view into the list view.  This is to
        // work around the search box from getting first focus and showing the cursor.
        getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mAppsRecyclerView.requestFocus();
                }
            }
        });


        // Load the all apps recycler view
//        mAppsRecyclerView = (AllAppsRecyclerView) findViewById(R.id.apps_list_view);
//        mAppsRecyclerView.setApps(mApps);
        mAppsRecyclerView.setLayoutManager(mLayoutManager);
        mAppsRecyclerView.setAdapter(mAdapter);
        mAppsRecyclerView.setHasFixedSize(true);
//        mAppsRecyclerView.addOnScrollListener(mElevationController);

        if (mItemDecoration != null) {
            mAppsRecyclerView.addItemDecoration(mItemDecoration);
        }

        // Precalculate the prediction icon and normal icon sizes
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                getResources().getDisplayMetrics().widthPixels, View.MeasureSpec.AT_MOST);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                getResources().getDisplayMetrics().heightPixels, View.MeasureSpec.AT_MOST);

       /* BubbleTextView icon = (BubbleTextView) layoutInflater.inflate(
                R.layout.all_apps_icon, this, false);
        icon.applyDummyInfo();
        icon.measure(widthMeasureSpec, heightMeasureSpec);
        BubbleTextView predIcon = (BubbleTextView) layoutInflater.inflate(
                R.layout.all_apps_prediction_bar_icon, this, false);
        predIcon.applyDummyInfo();
        predIcon.measure(widthMeasureSpec, heightMeasureSpec);
        mAppsRecyclerView.setPremeasuredIconHeights(predIcon.getMeasuredHeight(),
                icon.getMeasuredHeight());*/

    }

    public final View getContentView() {
        return mContent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Update the background and padding of the Apps view and children.  Instead of insetting the
     * container view, we inset the background and padding of the recycler view to allow for the
     * recycler view to handle touch events (for fast scrolling) all the way to the edge.
     */


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Determine if the key event was actual text, if so, focus the search bar and then dispatch
        // the key normally so that it can process this key event

        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return handleTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return handleTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mIconLastTouchPos.set((int) ev.getX(), (int) ev.getY());
                break;
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        // Return early if this is not initiated from a touch
        if (!v.isInTouchMode()) {
            return false;
        }

        return false;
    }


    /**
     * Handles the touch events to dismiss all apps when clicking outside the bounds of the
     * recycler view.
     */
    private boolean handleTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mContentBounds.isEmpty()) {
                    // Outset the fixed bounds and check if the touch is outside all apps
                    Rect tmpRect = new Rect(mContentBounds);
//                    tmpRect.inset(-grid.allAppsIconSizePx / 2, 0);
                    if (ev.getX() < tmpRect.left || ev.getX() > tmpRect.right) {
                        mBoundsCheckLastTouchDownPos.set(x, y);
                        return true;
                    }
                } else {
                    // Check if the touch is outside all apps
                    if (ev.getX() < getPaddingLeft() ||
                            ev.getX() > (getWidth() - getPaddingRight())) {
                        mBoundsCheckLastTouchDownPos.set(x, y);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mBoundsCheckLastTouchDownPos.x > -1) {
                    ViewConfiguration viewConfig = ViewConfiguration.get(getContext());
                    float dx = ev.getX() - mBoundsCheckLastTouchDownPos.x;
                    float dy = ev.getY() - mBoundsCheckLastTouchDownPos.y;
                    float distance = (float) Math.hypot(dx, dy);
                    if (distance < viewConfig.getScaledTouchSlop()) {
                        // The background was clicked, so just go home
                        Launcher launcher = (Launcher) getContext();
                        return true;
                    }
                }
                // Fall through
            case MotionEvent.ACTION_CANCEL:
                mBoundsCheckLastTouchDownPos.set(-1, -1);
                break;
        }
        return false;
    }
}