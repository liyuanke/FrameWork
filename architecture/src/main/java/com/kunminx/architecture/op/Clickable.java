package com.kunminx.architecture.op;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class Clickable extends ClickableSpan {
    private View.OnClickListener onClickListener;
    private boolean underlineText;
    private int color;

    public Clickable(View.OnClickListener onClickListener, int color) {
        this(color, false, onClickListener);
    }

    public Clickable(int color, boolean underlineText, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.underlineText = underlineText;
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        //设置文本的颜色
        ds.setColor(color);
        //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
        ds.setUnderlineText(underlineText);
    }

    @Override
    public void onClick(@NonNull View view) {
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }
}
