package com.mojang.minecraftpe;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.ArrayList;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/TextInputProxyEditTextbox.class */
public class TextInputProxyEditTextbox extends AppCompatEditText {
    private MCPEKeyWatcher _mcpeKeyWatcher;
    public int allowedLength;

    /* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/TextInputProxyEditTextbox$MCPEInputConnection.class */
    private class MCPEInputConnection extends InputConnectionWrapper {
        TextInputProxyEditTextbox textbox;
        final TextInputProxyEditTextbox this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MCPEInputConnection(TextInputProxyEditTextbox textInputProxyEditTextbox, InputConnection inputConnection, boolean z, TextInputProxyEditTextbox textInputProxyEditTextbox2) {
            super(inputConnection, z);
            this.this$0 = textInputProxyEditTextbox;
            this.textbox = textInputProxyEditTextbox2;
        }

        @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
        public boolean sendKeyEvent(KeyEvent keyEvent) {
            if (this.textbox.getText().length() != 0 || keyEvent.getAction() != 0 || keyEvent.getKeyCode() != 67) {
                return super.sendKeyEvent(keyEvent);
            }
            if (this.this$0._mcpeKeyWatcher == null) {
                return false;
            }
            this.this$0._mcpeKeyWatcher.onDeleteKeyPressed();
            return false;
        }
    }

    /* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/TextInputProxyEditTextbox$MCPEKeyWatcher.class */
    public interface MCPEKeyWatcher {
        boolean onBackKeyPressed();

        void onDeleteKeyPressed();
    }

    public TextInputProxyEditTextbox(Context context) {
        super(context);
        this._mcpeKeyWatcher = null;
    }

    private InputFilter createSingleLineFilter() {
        // from class: com.mojang.minecraftpe.TextInputProxyEditTextbox.1
// android.text.InputFilter
        return (charSequence, i, i2, spanned, i3, i4) -> {
            for (int i5 = i; i5 < i2; i5++) {
                if (charSequence.charAt(i5) == '\n') {
                    return charSequence.subSequence(i, i5);
                }
            }
            return null;
        };
    }

    private InputFilter createUnicodeFilter() {
        // from class: com.mojang.minecraftpe.TextInputProxyEditTextbox.2
// android.text.InputFilter
        return (charSequence, i, i2, spanned, i3, i4) -> {
            StringBuilder sb;
            int i5 = i;
            StringBuilder sb2 = null;
            while (true) {
                sb = sb2;
                if (i5 >= i2) {
                    break;
                }
                StringBuilder sb3 = sb;
                if (charSequence.charAt(i5) == 12288) {
                    sb3 = sb;
                    if (sb == null) {
                        sb3 = new StringBuilder(charSequence);
                    }
                    sb3.setCharAt(i5, ' ');
                }
                i5++;
                sb2 = sb3;
            }
            if (sb != null) {
                return sb.subSequence(i, i2);
            }
            return null;
        };
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        return new MCPEInputConnection(this, super.onCreateInputConnection(editorInfo), true, this);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
        ClipData primaryClip;
        ClipData.Item itemAt;
        if (i == 4 && keyEvent.getAction() == 1) {
            MCPEKeyWatcher mCPEKeyWatcher = this._mcpeKeyWatcher;
            if (mCPEKeyWatcher != null) {
                return mCPEKeyWatcher.onBackKeyPressed();
            }
            return false;
        }
        if (i != 50 || keyEvent.getAction() != 1 || !keyEvent.isCtrlPressed() || (primaryClip = ((ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip()) == null || (itemAt = primaryClip.getItemAt(0)) == null) {
            return super.onKeyPreIme(i, keyEvent);
        }
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        String string = getText().toString();
        setText(string.substring(0, selectionStart) + itemAt.getText().toString() + string.substring(selectionEnd));
        setSelection(Math.min(selectionStart + itemAt.getText().toString().length(), this.allowedLength));
        return true;
    }

    public void setOnMCPEKeyWatcher(MCPEKeyWatcher mCPEKeyWatcher) {
        this._mcpeKeyWatcher = mCPEKeyWatcher;
    }

    public void updateFilters(int i, boolean z) {
        this.allowedLength = i;
        ArrayList arrayList = new ArrayList();
        if (i != 0) {
            arrayList.add(new InputFilter.LengthFilter(this.allowedLength));
        }
        if (z) {
            arrayList.add(createSingleLineFilter());
        }
        arrayList.add(createUnicodeFilter());
        setFilters((InputFilter[]) arrayList.toArray(new InputFilter[arrayList.size()]));
    }
}
