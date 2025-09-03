package com.microsoft.xbox.xle.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import com.microsoft.xbox.toolkit.BackgroundThreadWaitor;
import com.microsoft.xbox.toolkit.anim.MAAS;
import com.microsoft.xbox.toolkit.anim.MAASAnimation;
import com.microsoft.xbox.toolkit.anim.XLEAnimation;
import com.microsoft.xbox.toolkit.anim.XLEAnimationPackage;
import com.microsoft.xbox.toolkit.ui.ScreenLayout;
import com.microsoft.xbox.xle.anim.XLEMAASAnimationPackageNavigationManager;
import com.microsoft.xbox.xle.ui.XLERootView;
import com.microsoft.xbox.xle.viewmodel.ViewModelBase;
import com.microsoft.xboxtcui.XboxTcuiSdk;
import java.lang.ref.WeakReference;


public abstract class ActivityBase extends ScreenLayout {
    private boolean showRightPane;
    private boolean showUtilityBar;
    protected ViewModelBase viewModel;

    protected int computeBottomMargin() {
        return 0;
    }

    protected boolean delayAppbarAnimation() {
        return false;
    }

    protected abstract String getActivityName();

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public String getRelativeId() {
        return null;
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public boolean getShouldShowAppbar() {
        return false;
    }

    public abstract void onCreateContentView();

    public void setHeaderName(String str) {
    }

    public ActivityBase() {
        this(0);
    }

    public ActivityBase(int i) {
        super(XboxTcuiSdk.getApplicationContext(), i);
        this.showUtilityBar = true;
        this.showRightPane = true;
    }

    public ActivityBase(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.showUtilityBar = true;
        this.showRightPane = true;
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onActivityResult(i, i2, intent);
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onStop() {
        if (getIsStarted()) {
            super.onStop();
            ViewModelBase viewModelBase = this.viewModel;
            if (viewModelBase != null) {
                viewModelBase.onSetInactive();
            }
            ViewModelBase viewModelBase2 = this.viewModel;
            if (viewModelBase2 != null) {
                viewModelBase2.onStop();
            }
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void forceRefresh() {
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.forceRefresh();
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onStart() {
        if (!getIsStarted()) {
            super.onStart();
            ViewModelBase viewModelBase = this.viewModel;
            if (viewModelBase != null) {
                viewModelBase.onStart();
            }
            ViewModelBase viewModelBase2 = this.viewModel;
            if (viewModelBase2 != null) {
                viewModelBase2.load();
            }
        }
        if (delayAppbarAnimation()) {
            return;
        }
        adjustBottomMargin(computeBottomMargin());
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onAnimateInStarted() {
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.forceUpdateViewImmediately();
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onAnimateInCompleted() {
        if (this.viewModel != null) {
            final WeakReference weakReference = new WeakReference(this.viewModel);
            BackgroundThreadWaitor.getInstance().postRunnableAfterReady(new Runnable() { // from class: com.microsoft.xbox.xle.app.activity.ActivityBase.1
                @Override // java.lang.Runnable
                public void run() {
                    ViewModelBase viewModelBase = (ViewModelBase) weakReference.get();
                    if (viewModelBase != null) {
                        viewModelBase.forceUpdateViewImmediately();
                    }
                }
            });
        }
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onAnimateInCompleted();
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void forceUpdateViewImmediately() {
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.forceUpdateViewImmediately();
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public XLEAnimationPackage getAnimateOut(boolean z) {
        MAASAnimation animation;
        XLEAnimation xLEAnimationCompile;
        View childAt = getChildAt(0);
        if (childAt == null || (animation = MAAS.getInstance().getAnimation("Screen")) == null || (xLEAnimationCompile = ((XLEMAASAnimationPackageNavigationManager) animation).compile(MAAS.MAASAnimationType.ANIMATE_OUT, z, childAt)) == null) {
            return null;
        }
        XLEAnimationPackage xLEAnimationPackage = new XLEAnimationPackage();
        xLEAnimationPackage.add(xLEAnimationCompile);
        return xLEAnimationPackage;
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public XLEAnimationPackage getAnimateIn(boolean z) {
        MAASAnimation animation;
        XLEAnimation xLEAnimationCompile;
        View childAt = getChildAt(0);
        if (childAt == null || (animation = MAAS.getInstance().getAnimation("Screen")) == null || (xLEAnimationCompile = ((XLEMAASAnimationPackageNavigationManager) animation).compile(MAAS.MAASAnimationType.ANIMATE_IN, z, childAt)) == null) {
            return null;
        }
        XLEAnimationPackage xLEAnimationPackage = new XLEAnimationPackage();
        xLEAnimationPackage.add(xLEAnimationCompile);
        return xLEAnimationPackage;
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onRestoreInstanceState(bundle);
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public boolean onBackButtonPressed() {
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            return viewModelBase.onBackButtonPressed();
        }
        return false;
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onSetActive() {
        super.onSetActive();
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onSetActive();
        }
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 8 && getXLERootView() != null && getXLERootView().getContentDescription() != null) {
            accessibilityEvent.getText().clear();
            accessibilityEvent.getText().add(getXLERootView().getContentDescription());
            return true;
        }
        return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onSetInactive() {
        super.onSetInactive();
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onSetInactive();
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onPause() {
        super.onPause();
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onPause();
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onApplicationPause() {
        super.onApplicationPause();
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onApplicationPause();
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onApplicationResume() {
        super.onApplicationResume();
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onApplicationResume();
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onResume() {
        super.onResume();
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onResume();
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onDestroy() {
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onDestroy();
        }
        this.viewModel = null;
        super.onDestroy();
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onTombstone() {
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onTombstone();
        }
        super.onTombstone();
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onRehydrate() {
        super.onRehydrate();
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onRehydrate();
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void onRehydrateOverride() {
        onCreateContentView();
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.onConfigurationChanged(configuration);
        }
    }

    private XLERootView getXLERootView() {
        if (getChildAt(0) instanceof XLERootView) {
            return (XLERootView) getChildAt(0);
        }
        return null;
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void adjustBottomMargin(int i) {
        if (getXLERootView() != null) {
            getXLERootView().setBottomMargin(i);
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void removeBottomMargin() {
        if (getXLERootView() != null) {
            getXLERootView().setBottomMargin(0);
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void resetBottomMargin() {
        if (getXLERootView() != null) {
            adjustBottomMargin(computeBottomMargin());
        }
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public String getName() {
        return getActivityName();
    }

    @Override // com.microsoft.xbox.toolkit.ui.ScreenLayout
    public void setScreenState(int i) {
        ViewModelBase viewModelBase = this.viewModel;
        if (viewModelBase != null) {
            viewModelBase.setScreenState(i);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearDisappearingChildren();
    }
}
