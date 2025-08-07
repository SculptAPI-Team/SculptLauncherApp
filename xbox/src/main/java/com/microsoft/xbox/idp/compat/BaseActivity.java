package com.microsoft.xbox.idp.compat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 07.01.2021
 *
 * @author Тимашков Иван
 * @author https://github.com/TimScriptov
 */

public abstract class BaseActivity extends AppCompatActivity {
    public boolean hasFragment(int i) {
        return getFragmentManager().findFragmentById(i) != null;
    }

    public void addFragment(int i, BaseFragment baseFragment) {
        getFragmentManager().beginTransaction().add(i, baseFragment).commit();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setOrientation();
    }

    @SuppressLint("WrongConstant")
    public void setOrientation() {
        if ((getApplicationContext().getResources().getConfiguration().screenLayout & 15) < 3) {
            setRequestedOrientation(1);
        }
    }
}
