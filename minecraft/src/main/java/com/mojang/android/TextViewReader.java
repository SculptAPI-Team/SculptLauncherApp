package com.mojang.android;

import android.widget.TextView;

/* loaded from: classes2-dex2jar.jar:com/mojang/android/TextViewReader.class */
public class TextViewReader implements StringValue {
    private TextView _view;

    public TextViewReader(TextView textView) {
        this._view = textView;
    }

    @Override // com.mojang.android.StringValue
    public String getStringValue() {
        return this._view.getText().toString();
    }
}
