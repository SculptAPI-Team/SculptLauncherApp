package com.microsoft.xbox.idp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.microsoft.xbox.idp.compat.BaseFragment;

import org.jetbrains.annotations.NotNull;
import org.thelauncher.xbox.R;

public class OfflineErrorFragment extends BaseFragment {
    public View onCreateView(@NotNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.xbid_fragment_error_offline, viewGroup, false);
    }
}
