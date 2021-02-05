package com.kunminx.architecture.op;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.kunminx.architecture.utils.LogUtils;

public class Spannable {
    protected final String TAG = this.getClass().getSimpleName();
    /**
     * 开始位置
     */
    private int start;
    /**
     * 结束位置
     */
    private int end;

    private SpannableString spannableString;

    /**
     * 设置位置
     *
     * @param start
     * @param end
     * @return
     */
    public Spannable setPosition(int start, int end) {
        this.start = start;
        this.end = end;
        if (TextUtils.isEmpty(spannableString)) {
            log("content is empty");
        } else if (start < 0) {
            log("start is less than 0");
        } else if (start > end) {
            log("start is more than end");
        } else if (end > spannableString.length()) {
            log("start is more than content.length");
        }
        return this;
    }

    public Spannable(CharSequence content) {
        this.spannableString = new SpannableString(content);
    }

    /**
     * 设置粗体
     *
     * @return
     */
    public Spannable setBold() {
        if (spannableString == null) {
            throw new NullPointerException("content has to be assigned");
        }
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
        return this;
    }

    /**
     * 设置斜粗体
     *
     * @return
     */
    public Spannable setBoldItalic() {
        if (spannableString == null) {
            throw new NullPointerException("content has to be assigned");
        }
        spannableString.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, 0);
        return this;
    }

    /**
     * 设置图片
     *
     * @param drawable
     * @return
     */
    public Spannable setImage(Drawable drawable) {
        if (spannableString == null) {
            throw new NullPointerException("content has to be assigned");
        }
        if (drawable != null) {
            spannableString.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), start, end, 0);
        }
        return this;
    }

    /**
     * 设置超链接
     *
     * @param urlSpan
     * @return
     */
    public Spannable setUrlSpan(String urlSpan) {
        if (spannableString == null) {
            throw new NullPointerException("content has to be assigned");
        }
        if (!TextUtils.isEmpty(urlSpan)) {
            spannableString.setSpan(new URLSpan(urlSpan), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /**
     * 设置可点击
     *
     * @param color
     * @param onClickListener
     * @return
     */
    public Spannable clickableSpan(int color, View.OnClickListener onClickListener) {
        return clickableSpan(color, false, onClickListener);
    }

    /**
     * 设置可点击
     *
     * @param color
     * @param underlineText   是否显示下划线
     * @param onClickListener
     * @return
     */
    public Spannable clickableSpan(int color, boolean underlineText, View.OnClickListener onClickListener) {
        if (spannableString == null) {
            throw new NullPointerException("content has to be assigned");
        }
        spannableString.setSpan(new Clickable(color, underlineText, onClickListener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public Spannable setSize(int size) {
        if (spannableString == null) {
            throw new NullPointerException("content has to be assigned");
        }
        if (start >= 0 && end > start) {
            spannableString.setSpan(new AbsoluteSizeSpan(size), start, end, 0);
        }
        return this;
    }

    /**
     * 设置前景色
     *
     * @param foregroundColor
     * @return
     */
    public Spannable setForegroundColor(int foregroundColor) {
        if (spannableString == null) {
            throw new NullPointerException("content has to be assigned");
        }
        spannableString.setSpan(new ForegroundColorSpan(foregroundColor), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置背景色
     *
     * @param backgroundColor
     * @return
     */
    public Spannable setBackgroundColor(int backgroundColor) {
        if (spannableString == null) {
            throw new NullPointerException("content has to be assigned");
        }
        spannableString.setSpan(new BackgroundColorSpan(backgroundColor), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置下划线
     */
    public Spannable setUnderlineSpan() {
        if (spannableString == null) {
            throw new NullPointerException("content has to be assigned");
        }
        spannableString.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    private void log(String msg) {
        LogUtils.INFO(msg);
    }

    /**
     * 设置中横线
     *
     * @return
     */
    public Spannable setStrikethroughSpan() {
        if (spannableString == null) {
            throw new NullPointerException("content has to be assigned");
        }
        spannableString.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    public SpannableString build() {
        return spannableString;
    }

    public static SpannableString getUnit(String content, String unit) {
        if (content == null) {
            content = "";
        }
        String value = content + unit;
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new RelativeSizeSpan(0.9f), content.length(), content.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//一半大小
        spannableString.setSpan(new RelativeSizeSpan(0.6f), content.length() + 1, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//一半大小
        spannableString.setSpan(new SuperscriptSpan(), content.length() + 1, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   //上标
        return spannableString;
    }

    public static SpannableString getM2(String content) {
        return getUnit(content, "m2");
    }

    public static SpannableString getM3(String content) {
        return getUnit(content, "m3");
    }

}
