package com.kunminx.architecture.bean;

import androidx.annotation.Nullable;

/**
 * 是否加载遮罩
 */
public class Mask {
    private boolean value;

    public Mask(boolean value) {
        this.value = value;
    }

    public boolean isShow() {
        return value == true;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return false;
    }
}
