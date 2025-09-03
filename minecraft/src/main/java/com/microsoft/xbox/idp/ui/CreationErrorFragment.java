package com.microsoft.xbox.idp.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.microsoft.xbox.idp.compat.BaseFragment;
import com.microsoft.xbox.idp.model.Const;

import org.sculptlauncher.minecraft.R;

public class CreationErrorFragment extends BaseFragment {
    public static final String TAG = "CreationErrorFragment";

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.xbid_fragment_error_creation, viewGroup, false);
    }

    @Override // android.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        UiUtil.ensureClickableSpanOnUnderlineSpan((TextView) view.findViewById(R.id.xbid_error_message), R.string.xbid_creation_error_android, new ClickableSpan() { // from class: com.microsoft.xbox.idp.ui.CreationErrorFragment.1
            @Override // android.text.style.ClickableSpan
            public void onClick(View view2) {
                Log.d(CreationErrorFragment.TAG, "onClick");
                try {
                    CreationErrorFragment.this.startActivity(new Intent("android.intent.action.VIEW", Const.URL_XBOX_COM));
                } catch (ActivityNotFoundException e) {
                    Log.e(CreationErrorFragment.TAG, e.getMessage());
                }
            }
        });
    }
}
