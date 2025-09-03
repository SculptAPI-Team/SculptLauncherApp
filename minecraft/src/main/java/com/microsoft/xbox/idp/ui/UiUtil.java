package com.microsoft.xbox.idp.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.microsoft.xbox.idp.compat.BaseActivity;
import com.microsoft.xbox.idp.compat.BaseFragment;

import org.sculptlauncher.minecraft.R;

public final class UiUtil {
    private static final String TAG = "UiUtil";

    public static boolean ensureHeaderFragment(BaseActivity baseActivity, int i, Bundle bundle) {
        return ensureFragment(HeaderFragment.class, baseActivity, i, bundle);
    }

    public static boolean ensureErrorFragment(BaseActivity baseActivity, ErrorActivity.ErrorScreen errorScreen) {
        if (baseActivity.hasFragment(R.id.xbid_body_fragment)) {
            return false;
        }
        return ensureFragment(errorScreen.errorFragmentClass, baseActivity, R.id.xbid_body_fragment, baseActivity.getIntent().getExtras());
    }

    public static boolean ensureErrorButtonsFragment(BaseActivity baseActivity, ErrorActivity.ErrorScreen errorScreen) {
        if (baseActivity.hasFragment(R.id.xbid_error_buttons)) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ErrorButtonsFragment.ARG_LEFT_ERROR_BUTTON_STRING_ID, errorScreen.leftButtonTextId);
        return ensureFragment(ErrorButtonsFragment.class, baseActivity, R.id.xbid_error_buttons, bundle);
    }

    public static void ensureClickableSpanOnUnderlineSpan(TextView textView, int i, ClickableSpan clickableSpan) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(Html.fromHtml(textView.getResources().getString(i)));
        UnderlineSpan[] underlineSpanArr = (UnderlineSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), UnderlineSpan.class);
        if (underlineSpanArr != null && underlineSpanArr.length > 0) {
            UnderlineSpan underlineSpan = underlineSpanArr[0];
            spannableStringBuilder.setSpan(clickableSpan, spannableStringBuilder.getSpanStart(underlineSpan), spannableStringBuilder.getSpanEnd(underlineSpan), 33);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        textView.setText(spannableStringBuilder);
    }

    public static boolean canScroll(ScrollView scrollView) {
        View childAt = scrollView.getChildAt(0);
        if (childAt == null) {
            return false;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
        return scrollView.getHeight() < (marginLayoutParams.topMargin + childAt.getHeight()) + marginLayoutParams.bottomMargin;
    }

    private static boolean ensureFragment(Class<? extends BaseFragment> cls, BaseActivity baseActivity, int i, Bundle bundle) {
        if (baseActivity.hasFragment(i)) {
            return false;
        }
        try {
            BaseFragment baseFragmentNewInstance = cls.newInstance();
            baseFragmentNewInstance.setArguments(bundle);
            baseActivity.addFragment(i, baseFragmentNewInstance);
            return true;
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
            return false;
        } catch (InstantiationException e2) {
            Log.e(TAG, e2.getMessage());
            return false;
        }
    }
}
