package com.microsoft.xbox.idp.compat;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public boolean hasFragment(int i) {
        return getFragmentManager().findFragmentById(i) != null;
    }

    public void addFragment(int i, BaseFragment baseFragment) {
        getFragmentManager().beginTransaction().add(i, baseFragment).commit();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setOrientation();
    }

    public void setOrientation() {
        if ((getApplicationContext().getResources().getConfiguration().screenLayout & 15) < 3) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }
}
