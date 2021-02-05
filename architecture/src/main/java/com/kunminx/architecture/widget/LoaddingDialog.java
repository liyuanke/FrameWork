package com.kunminx.architecture.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.kunminx.architecture.R;


/**
 * @author lyk
 * @date 2019/10/16.
 * GitHub：
 * email：
 * description：
 */
public class LoaddingDialog extends AlertDialog.Builder {
    private AlertDialog mDialog;

    public LoaddingDialog(@NonNull Context context) {
        super(context, R.style.full_screen_dialog);
        initView(context);
    }

    private void initView(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_loadding, null);
        setView(layout);
        mDialog = create();
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setDimAmount(0);
        }
        setCanceledOnTouchOutside(false);
    }

    public void setCanceledOnTouchOutside(boolean enable) {
        mDialog.setCanceledOnTouchOutside(enable);
    }

    public void dissmiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    @Override
    public AlertDialog show() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
        return mDialog;
    }
}
