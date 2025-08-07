package com.mojang.minecraftpe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/PopupView.class */
public class PopupView {
    private View mContentView;
    private Context mContext;
    private int mHeight;
    private int mOriginX;
    private int mOriginY;
    private View mParentView;
    private View mPopupView;
    private int mWidth;
    private WindowManager mWindowManager;

    public PopupView(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    private void addPopupView() {
        this.mPopupView = this.mContentView;
        WindowManager.LayoutParams layoutParamsCreatePopupLayout = createPopupLayout(this.mParentView.getWindowToken());
        setLayoutRect(layoutParamsCreatePopupLayout);
        invokePopup(layoutParamsCreatePopupLayout);
    }

    private int computeFlags(int i) {
        return i | 32;
    }

    private WindowManager.LayoutParams createPopupLayout(IBinder iBinder) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.format = -3;
        layoutParams.flags = computeFlags(layoutParams.flags);
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        layoutParams.token = iBinder;
        layoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;
        layoutParams.setTitle("PopupWindow:" + Integer.toHexString(hashCode()));
        layoutParams.windowAnimations = -1;
        return layoutParams;
    }

    private void invokePopup(WindowManager.LayoutParams layoutParams) {
        layoutParams.packageName = this.mContext.getPackageName();
        this.mWindowManager.addView(this.mPopupView, layoutParams);
        this.mParentView.requestFocus();
    }

    private void preparePopup(WindowManager.LayoutParams layoutParams) {
    }

    private void removePopupView() {
        try {
            this.mParentView.requestFocus();
            this.mWindowManager.removeView(this.mPopupView);
        } catch (Exception e) {
        }
    }

    @SuppressLint("WrongConstant")
    private void setLayoutRect(WindowManager.LayoutParams layoutParams) {
        layoutParams.width = this.mWidth;
        layoutParams.height = this.mHeight;
        layoutParams.x = this.mOriginX;
        layoutParams.y = this.mOriginY;
        layoutParams.gravity = 51;
    }

    public void dismiss() {
        if (this.mPopupView != null) {
            removePopupView();
            View view = this.mPopupView;
            View view2 = this.mContentView;
            if (view != view2 && (view instanceof ViewGroup)) {
                ((ViewGroup) view).removeView(view2);
            }
            this.mPopupView = null;
        }
    }

    public boolean getVisible() {
        View view = this.mPopupView;
        return (view == null || view.getParent() == null) ? false : true;
    }

    public void setContentView(View view) {
        this.mContentView = view;
    }

    public void setHeight(int i) {
        this.mHeight = i;
    }

    public void setParentView(View view) {
        this.mParentView = view;
    }

    public void setRect(int i, int i2, int i3, int i4) {
        this.mWidth = i2 - i;
        this.mHeight = i4 - i3;
        this.mOriginX = i;
        this.mOriginY = i3;
    }

    public void setVisible(boolean z) {
        if (z != getVisible()) {
            if (z) {
                addPopupView();
            } else {
                removePopupView();
            }
        }
    }

    public void setWidth(int i) {
        this.mWidth = i;
    }

    public void update() {
        if (getVisible()) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mPopupView.getLayoutParams();
            int iComputeFlags = computeFlags(layoutParams.flags);
            if (iComputeFlags != layoutParams.flags) {
                layoutParams.flags = iComputeFlags;
            }
            setLayoutRect(layoutParams);
            this.mWindowManager.updateViewLayout(this.mPopupView, layoutParams);
        }
    }
}
