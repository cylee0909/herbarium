package com.cylee.androidlib.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cylee.lib.R;

/**
 * 用于切换显示“主页面”、“正在加载中……”、“加载出错”
 * 支持替换所有的View，只需要传入需要被替换的View的id即可替换
 *
 * @author chenmiao
 */
public class SwitchViewUtil {

    public static final String TAG = "SwitchListViewUtil";

    private View mMainView;
    private Context mContext;
    private int mCurrentViewIndex;
    private ViewGroup.LayoutParams mLayoutParams;
    private View mLastView;
    private View.OnClickListener onClickListener;

    public SwitchViewUtil(Activity activity, int mainViewId, View.OnClickListener onClickListener) {
        this(activity, activity.findViewById(mainViewId), onClickListener);
    }

    public SwitchViewUtil(Context context, View mainView, View.OnClickListener onClickListener) {
        mContext = context;
        mMainView = mainView;
        if (mMainView == null) {
            throw new RuntimeException();
        }
        mCurrentViewIndex = getParentView(mMainView).indexOfChild(mMainView);
        mLayoutParams = mMainView.getLayoutParams();
        this.onClickListener = onClickListener;
    }

    public void showCustomView(int viewId) {
        showCustomView(LayoutInflater.from(mContext).inflate(viewId, null));
    }

    public void showView(ViewType viewType) {
        showView(viewType, null);
    }

    public void showView(ViewType viewType, View newView) {
        View view = null;
        if (viewType.equals(ViewType.MAIN_VIEW)) {
            showMainView();
        } else if (viewType.equals(ViewType.ERROR_VIEW)) {
            view = LayoutInflater.from(mContext).inflate(R.layout.common_layout_listview_error, null);
        } else if (viewType.equals(ViewType.LOADING_VIEW)) {
            if (newView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.common_layout_listview_loading, null);
            } else {
                view = newView;
            }
        } else if (viewType.equals(ViewType.EMPTY_VIEW)) {
            if (newView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.common_layout_listview_empty, null);
            } else {
                view = newView;

            }
        } else if (viewType.equals(ViewType.NO_NETWORK_VIEW)) {
            if (newView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.common_layout_listview_no_network, null);
            } else {
                view = newView;
            }
        } else if (viewType.equals(ViewType.LOADING_VIEW)) {
            if (newView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.common_layout_listview_loading, null);
            } else {
                view = newView;
            }
        }
        if (onClickListener != null) {
            view.setOnClickListener(onClickListener);
        }
        showCustomView(view);
    }

    /**
     * 显示自定义View，调用dismissCustomView()取消显示自定义View
     *
     * @param view
     */
    public void showCustomView(View view) {
        if (view == mLastView) {
            return;
        }
        if (mLastView != null && mLastView != mMainView) {
            getParentView(mMainView).removeView(mLastView);
            mLastView = null;
        }
        if (view != null && view != mMainView) {
            mMainView.setVisibility(View.GONE);
            getParentView(mMainView).addView(view, mCurrentViewIndex, mLayoutParams);
        } else {
            mMainView.setVisibility(View.VISIBLE);
        }
        mLastView = view;
    }

    public void showMainView() {
        if (mLastView != null) {
            getParentView(mMainView).removeView(mLastView);
            mLastView = null;
        }
        mMainView.setVisibility(View.VISIBLE);
    }

    private ViewGroup getParentView(View mainView) {
        return (ViewGroup) mainView.getParent();
    }


    public enum ViewType {
        MAIN_VIEW, //主页面
        ERROR_VIEW, //“加载出错”页面
        LOADING_VIEW, //"正在加载"页面
        EMPTY_VIEW, //"加载为空"页面
        NO_NETWORK_VIEW, //"无网"页面
    }
}
