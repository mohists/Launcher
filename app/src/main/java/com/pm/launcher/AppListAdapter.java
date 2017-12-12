package com.pm.launcher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ResolveInfo;
import android.database.Observable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anarchy.classify.adapter.BaseMainAdapter;
import com.anarchy.classify.adapter.BaseSubAdapter;
import com.anarchy.classify.simple.PrimitiveSimpleAdapter;
import com.anarchy.classify.simple.SimpleAdapter;
import com.pm.launcher.databinding.ItemFolderBinding;
import com.pm.launcher.mode.AppInfo;
import com.pm.launcher.mode.FolderInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by puming on 2017/2/16.
 */

public class AppListAdapter extends PrimitiveSimpleAdapter<FolderInfo,AppListAdapter.ViewHolder> {
    List<ResolveInfo> mList;
    Context mContext;

    private List<AppInfo> mMockSource;
    /**
     * 存储被选中的Item
     */
    private List<AppInfo> mCheckedData = new ArrayList<>();
    /**
     * Item是否被选中
     */
    private boolean mMockSourceChanged;
    private List<FolderInfo> mLastMockGroup;
    private boolean mEditMode;
    private boolean mSubEditMode;
    /**
     *
     */
    private int[] mDragPosition = new int[2];
    private MyObservable mObservable = new MyObservable();
    private SubObserver mSubObserver = new SubObserver(mObservable);
    private DialogInterface.OnDismissListener mDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if(mObservable.isRegister(mSubObserver)) {
                mObservable.unregisterObserver(mSubObserver);
            }
            mSubEditMode = false;
        }
    };

    public void registerObserver(MyObserver observer) {
        mObservable.registerObserver(observer);
    }


    public List<AppInfo> getMockSource() {
        return mMockSource;
    }

    public void setMockSource(List<AppInfo> mockSource) {
        mMockSource = mockSource;
        notifyDataSetChanged();
    }

    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected int getItemType(int parentPosition, int subPosition) {
        return super.getItemType(parentPosition, subPosition);
    }

    @Override
    public View getView(ViewGroup parent, View convertView, int mainPosition, int subPosition) {
        View result;
        if (convertView != null) {
            result = convertView;
        } else {
            result = new View(parent.getContext());
        }
        try {
//            int color = ((FolderInfo) mMockSource.get(mainPosition)).getChild(subPosition).getColor();
            result.setBackgroundColor(Color.DKGRAY);
        } catch (Exception e) {
            //something wrong
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected int getItemCount() {
        return mMockSource == null ? 0 : mMockSource.size();
    }

    @Override
    protected int getSubItemCount(int parentPosition) {
        if(parentPosition < mMockSource.size()) {
            AppInfo mockData = mMockSource.get(parentPosition);
            if (mockData instanceof FolderInfo) {
                int subCount = ((FolderInfo) mockData).getChildCount();
                return subCount;
            }
        }
        return 0;
    }

    @Override
    protected FolderInfo getSubSource(int parentPosition) {
        AppInfo mockData = mMockSource.get(parentPosition);
        if (mockData instanceof FolderInfo) return (FolderInfo) mockData;
        return null;
    }

    @Override
    protected boolean canExplodeItem(int position, View pressedView) {
        return mMockSource.get(position) instanceof FolderInfo;
    }

    @Override
    protected void onMove(int selectedPosition, int targetPosition) {
        mMockSource.add(targetPosition, mMockSource.remove(selectedPosition));
        mMockSourceChanged = true;
    }

    @Override
    protected void onSubMove(FolderInfo folderInfo, int selectedPosition, int targetPosition) {
        folderInfo.addChild(targetPosition, folderInfo.removeChild(selectedPosition));
    }

    @Override
    protected boolean canMergeItem(int selectPosition, int targetPosition) {
        AppInfo select = mMockSource.get(selectPosition);
        return !(select instanceof FolderInfo);
    }

    @Override
    protected void onMerged(int selectedPosition, int targetPosition) {
        AppInfo target = mMockSource.get(targetPosition);
        AppInfo select = mMockSource.remove(selectedPosition);
        if (target instanceof FolderInfo) {
            ((FolderInfo) target).addChild(0, select);
        } else {
            //合并成为文件夹状态
            FolderInfo group = new FolderInfo();
            group.addChild(select);
            group.addChild(target);
            group.setCategory(generateNewCategoryTag());
            targetPosition = mMockSource.indexOf(target);
            mMockSource.remove(targetPosition);
            mMockSource.add(targetPosition, group);
        }
        mMockSourceChanged = true;
    }

    @Override
    protected int onLeaveSubRegion(int parentPosition, FolderInfo folderInfo, int selectedPosition) {
        if(mObservable.isRegister(mSubObserver)) mObservable.unregisterObserver(mSubObserver);
        //从副层级移除并添加到主层级第一个位置上
        AppInfo mockData = folderInfo.removeChild(selectedPosition);
        mMockSource.add(0, mockData);
        if (folderInfo.getChildCount() == 0) {
            int p = mMockSource.indexOf(folderInfo);
            mMockSource.remove(p);
        }
        mMockSourceChanged = true;
        return 0;
    }

    @Override
    protected void onBindMainViewHolder(ViewHolder holder, int position) {
        holder.bind(mMockSource.get(position), mEditMode);
    }

    @Override
    protected void onBindSubViewHolder(ViewHolder holder, int mainPosition, int subPosition) {
        holder.bind(((FolderInfo) mMockSource.get(mainPosition)).getChild(subPosition), mEditMode);
    }

    @Override
    protected void onDragStart(ViewHolder viewHolder, int parentIndex, int index) {
        if (!mEditMode) {
            //如果当前不为可编辑状态
            AppInfo mockData = index == -1 ? mMockSource.get(parentIndex) : ((FolderInfo) mMockSource.get(parentIndex)).getChild(index);
            if (mockData != null) {
                mockData.setChecked(true);
                mCheckedData.add(mockData);
                mObservable.notifyItemCheckChanged(true);
                viewHolder.getBinding().folderCheckBox.setVisibility(View.VISIBLE);
                viewHolder.getBinding().folderCheckBox.setBackgroundResource(R.drawable.ic_checked);
            }
        }
    }

    @Override
    protected void onDragAnimationEnd(ViewHolder viewHolder, int parentIndex, int index) {
        if (!mEditMode) {
            setEditMode(true);
        }
    }

    @Override
    protected void onSubDialogShow(Dialog dialog, int parentPosition) {
        dialog.setOnDismissListener(mDismissListener);
        //当次级窗口显示时需要修改标题
        final ViewGroup contentView = (ViewGroup) dialog.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
//        final TextView selectAll = (TextView) contentView.findViewById(R.id.text_select_all);
//        TextView title = (TextView) contentView.findViewById(R.id.text_title);
        final EditText editText = (EditText) contentView.findViewById(R.id.edit_title);
        FrameLayout subContainer = (FrameLayout) contentView.findViewById(R.id.sub_container);
        final FolderInfo mockDataGroup = (FolderInfo) mMockSource.get(parentPosition);
        mSubObserver.setBindResource(mockDataGroup, null, getMainAdapter(),getSubAdapter(),parentPosition);
        if(!mObservable.isRegister(mSubObserver)) mObservable.registerObserver(mSubObserver);
        int subEditMode = mSubEditMode ? View.GONE : View.VISIBLE;
//        selectAll.setVisibility(mEditMode ? subEditMode : View.GONE);
//        title.setText(String.valueOf(mockDataGroup.getCategory()));

        /*if(Build.VERSION.SDK_INT >= 19) {
            title.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    mSubEditMode = true;
                    selectAll.setVisibility(View.GONE);
                    editText.setText(String.valueOf(mockDataGroup.getCategory()));
                    editText.setSelection(0,editText.getText().toString().length());
                    int originWidth = editText.getWidth();
                    editText.setWidth(0);
                    TransitionManager.beginDelayedTransition(contentView);
                    editText.setWidth(originWidth);
                }
            });
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    switch (actionId){
                        case KeyEvent.KEYCODE_ENTER:
                            break;
                    }
                    return false;
                }
            });
        }*/

    }

    protected void onItemClick(ViewHolder viewHolder, int parentIndex, int index) {
        if (mEditMode) {
            final AppInfo mockData = index == -1 ? mMockSource.get(parentIndex) : ((FolderInfo) mMockSource.get(parentIndex)).getChild(index);
            if (!(mockData instanceof FolderInfo)) {
                //执行check动画
                mockData.setChecked(!mockData.isChecked());
                mCheckedData.add(mockData);
                //通知
                mObservable.notifyItemCheckChanged(mockData.isChecked());
                if (index != -1) {
                    notifyItemChanged(parentIndex);
                }
                final ItemFolderBinding binding = viewHolder.getBinding();
                binding.folderCheckBox.setScaleX(0f);
                binding.folderCheckBox.setScaleY(0f);
                binding.folderCheckBox.animate().scaleX(1f).scaleY(1f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        binding.folderCheckBox.setScaleX(1f);
                        binding.folderCheckBox.setScaleY(1f);
                        binding.folderCheckBox.animate().setListener(null);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        binding.folderCheckBox.setScaleX(1f);
                        binding.folderCheckBox.setScaleY(1f);
                        binding.folderCheckBox.animate().setListener(null);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        binding.folderCheckBox.setBackgroundResource(mockData.isChecked() ? R.drawable.ic_checked : R.drawable.ic_unchecked);
                    }
                });
            }
        }
    }


    /**
     * 返回当前拖拽的view 在adapter中的位置
     *
     * @return 返回int[0] 主层级位置 如果为 -1 则当前没有拖拽的item
     * int[1] 副层级位置 如果为 -1 则当前没有拖拽副层级的item
     */
    @NonNull
    public int[] getCurrentDragAdapterPosition() {
        mDragPosition[0] = getMainAdapter().getDragPosition();
        mDragPosition[1] = getSubAdapter().getDragPosition();
        return mDragPosition;
    }

    /**
     * @return 如果当前拖拽的为单个书籍 则返回 其他情况返回null
     */
    @Nullable
    AppInfo getCurrentSingleDragData() {
        int[] position = getCurrentDragAdapterPosition();
        if (position[0] == -1) return null;
        if (position[1] == -1) {
            AppInfo mockData = mMockSource.get(position[0]);
            if (mockData instanceof FolderInfo) return null;
            return mockData;
        } else {
            return ((FolderInfo) mMockSource.get(position[0])).getChild(position[1]);
        }
    }

    /**
     *
     */
    public void removeAllCheckedItem() {
        if (mCheckedData.size() == 0) return;
        for (AppInfo data : mCheckedData) {
            if (data.getFolder() != null) {
                FolderInfo parent = data.getFolder();
                parent.removeChild(data);
                if (parent.getChildCount() == 0) {
                    mMockSource.remove(parent);
                }
            } else {
                mMockSource.remove(data);
            }
        }
        notifyDataSetChanged();
        getSubAdapter().notifyDataSetChanged();
        mObservable.notifyItemRestore();
        mObservable.notifyItemHideSubDialog();
    }

    /**
     * 添加数据
     *
     * @param mockData
     */
    public void addData(List<AppInfo> mockData) {
        if (mMockSource == null) mMockSource = new ArrayList<>();
        mMockSource.addAll(mockData);
        notifyDataSetChanged();
    }

    /**
     * 设置是否在可编辑状态下
     *
     * @param editMode
     */
    public void setEditMode(boolean editMode) {
        mEditMode = editMode;
        if (!editMode) {
            if (mCheckedData.size() > 0) {
                for (AppInfo data : mCheckedData) {
                    data.setChecked(false);
                }
                mCheckedData.clear();
            }
            mObservable.notifyItemRestore();
        }
        notifyDataSetChanged();
        getSubAdapter().notifyDataSetChanged();
        mObservable.notifyItemEditModeChanged(editMode);
    }

    public boolean isEditMode() {
        return mEditMode;
    }

    public List<FolderInfo> getMockGroup() {
        if (mMockSource == null) return null;
        if (mLastMockGroup != null && !mMockSourceChanged) {
            return mLastMockGroup;
        } else {
            List<FolderInfo> result = new ArrayList<>();
            for (AppInfo mockData : mMockSource) {
                if (mockData instanceof FolderInfo) {
                    result.add((FolderInfo) mockData);
                }
            }
            mMockSourceChanged = false;
            mLastMockGroup = result;
            return result;
        }
    }

    /**
     * 生成新的分类标签
     *
     * @return 新的分类标签
     */
    private String generateNewCategoryTag() {
        //生成默认分类标签
        List<FolderInfo> mockDataGroups = getMockGroup();
        if (mockDataGroups.size() > 0) {
            int serialNumber = 1;
            int[] mHoldNumber = null;
            for (FolderInfo temp : mockDataGroups) {
                if (temp.getCategory().startsWith("分类")) {
                    //可能是自动生成的标签
                    String pendingStr = temp.getCategory().substring(2);
                    if (!TextUtils.isEmpty(pendingStr) && TextUtils.isDigitsOnly(pendingStr)) {
                        //尝试转换为整数
                        try {
                            int serialCategory = Integer.parseInt(pendingStr);
                            if (mHoldNumber == null) {
                                mHoldNumber = new int[1];
                                mHoldNumber[0] = serialCategory;
                            } else {
                                mHoldNumber = Arrays.copyOf(mHoldNumber, mHoldNumber.length + 1);
                                mHoldNumber[mHoldNumber.length - 1] = serialCategory;
                            }
                        } catch (NumberFormatException e) {
                            //nope
                        }
                    }
                }
            }
            if (mHoldNumber != null) {
                //有自动生成的标签
                Arrays.sort(mHoldNumber);
                for (int serial : mHoldNumber) {
                    if (serial < serialNumber) continue;
                    if (serial == serialNumber) {
                        //已经被占用 自增1
                        serialNumber++;
                    } else {
                        break;
                    }
                }
            }
            return "分类" + serialNumber;
        } else {
            return "分类1";
        }
    }

    /**
     *  ViewHolder
     *
     */
    static class ViewHolder extends PrimitiveSimpleAdapter.ViewHolder {
        private ItemFolderBinding mBinding;

        ViewHolder(View itemView) {
            super(itemView);
            mBinding = ItemFolderBinding.bind(itemView);
        }

        ItemFolderBinding getBinding() {
            return mBinding;
        }

        /**
         *
         * @param appInfo
         * @param inEditMode
         */
        void bind(AppInfo appInfo, boolean inEditMode) {
            if (inEditMode) {
                if (appInfo instanceof FolderInfo) {
                    mBinding.cardView.setVisibility(View.GONE);
                    int count = ((FolderInfo) appInfo).getCheckedCount();
                    if (count > 0) {
                        mBinding.folderCheckBox.setVisibility(View.VISIBLE);
                        mBinding.folderCheckBox.setText(count + "");
//                        mBinding.iReaderFolderCheckBox.setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_number_bg));
                        mBinding.folderCheckBox.setBackgroundDrawable(new ColorDrawable(Color.RED));
                    } else {
                        mBinding.folderCheckBox.setVisibility(View.GONE);
                    }
                } else {
                    mBinding.folderView.setVisibility(View.GONE);
                    Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), appInfo.isChecked() ? R.drawable.ic_checked : R.drawable.ic_unchecked);
                    mBinding.folderCheckBox.setText("");
                    mBinding.folderCheckBox.setVisibility(View.VISIBLE);
                    mBinding.folderCheckBox.setBackgroundDrawable(drawable);
                }
            } else {
                mBinding.folderCheckBox.setVisibility(View.GONE);
            }
            if (appInfo instanceof FolderInfo) {
                mBinding.folderView.setVisibility(View.VISIBLE);
                mBinding.folderGridLayout.setVisibility(View.VISIBLE);
                mBinding.folderTag.setVisibility(View.VISIBLE);
                mBinding.folderTag.setText(((FolderInfo) appInfo).getCategory());
                mBinding.folderContent.setVisibility(View.GONE);
            } else {
                mBinding.cardView.setVisibility(View.VISIBLE);
                mBinding.folderGridLayout.setVisibility(View.INVISIBLE);
                mBinding.folderTag.setVisibility(View.GONE);
                mBinding.folderContent.setBackgroundColor(appInfo.getColor());
                mBinding.folderContent.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 可观察的类型
     */
    static class MyObservable extends Observable<MyObserver> {

        /**
         *
         * @param observer
         * @return
         */
        public boolean isRegister(MyObserver observer){
            return mObservers.contains(observer);
        }


        /**
         *
         * @param isChecked
         */
        public void notifyItemCheckChanged(boolean isChecked) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChecked(isChecked);
            }
        }

        /**
         *
         * @param editMode
         */
        public void notifyItemEditModeChanged(boolean editMode) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onEditChanged(editMode);
            }
        }

        /**
         *
         */
        public void notifyItemRestore() {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onRestore();
            }
        }

        /**
         *
         */
        public void notifyItemHideSubDialog(){
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onHideSubDialog();
            }
        }
    }

    /**
     * 观察者抽象类，需要具体的观察者实现相应的行为逻辑
     */
    public static abstract class MyObserver {
        /**
         * 选中状态改变时回调
         * @param isChecked true 选中 ，false 未选中
         */
        public void onChecked(boolean isChecked) {
        }

        /**
         * 可编辑状态改变时回调
         * @param inEdit true 可以编辑，false 不可以编辑
         */
        public void onEditChanged(boolean inEdit) {
        }

        /**
         * 恢复状态时回调
         */
        public void onRestore() {
        }

        /**
         * 当需要次级窗口时回调
         */
        public void onHideSubDialog(){
        }
    }

    /**
     * 具体的观察者类型
     */
    static class SubObserver extends MyObserver {
        final MyObservable mObservable;
        FolderInfo mGroup;
        TextView selectAll;
        BaseSubAdapter mSubAdapter;
        BaseMainAdapter mMainAdapter;
        int parentPosition;
        boolean mLastIsAllSelect;

        SubObserver(@NonNull MyObservable observable) {
            mObservable = observable;
        }

        View.OnClickListener allSelectListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = mGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    AppInfo child = mGroup.getChild(i);
                    if(!child.isChecked()){
                        child.setChecked(true);
                        mObservable.notifyItemCheckChanged(true);
                    }
                }
                mSubAdapter.notifyDataSetChanged();
                mMainAdapter.notifyItemChanged(parentPosition);
            }
        };
        View.OnClickListener cancelSelectListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int childCount = mGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    AppInfo child = mGroup.getChild(i);
                    if(child.isChecked()){
                        child.setChecked(false);
                        mObservable.notifyItemCheckChanged(false);
                    }
                }
                mSubAdapter.notifyDataSetChanged();
                mMainAdapter.notifyItemChanged(parentPosition);
            }
        };

        void setBindResource(FolderInfo source, TextView bindView,BaseMainAdapter mainAdapter ,BaseSubAdapter subAdapter,int parentPosition) {
            mGroup = source;
            selectAll = bindView;
            mSubAdapter = subAdapter;
            mMainAdapter = mainAdapter;
            this.parentPosition = parentPosition;
            updateBind(true);
        }


        @Override
        public void onChecked(boolean isChecked) {
            updateBind(false);
        }

        private void updateBind(boolean force) {
            boolean isAllSelect = mGroup.getChildCount() == mGroup.getCheckedCount();
            if(force){
                updateBindInternal(isAllSelect);
                return;
            }
            if (mLastIsAllSelect != isAllSelect) {
                updateBindInternal(isAllSelect);
            }
        }

        private void updateBindInternal(boolean isAllSelect){
            mLastIsAllSelect = isAllSelect;
//            selectAll.setText(isAllSelect ? "取消" : "全选");
//            selectAll.setOnClickListener(isAllSelect? cancelSelectListener : allSelectListener);
        }

    }

}
