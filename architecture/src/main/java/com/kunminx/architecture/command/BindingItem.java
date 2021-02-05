package com.kunminx.architecture.command;

import androidx.annotation.LayoutRes;

public abstract class BindingItem {
    @LayoutRes
    public abstract int getLayoutId(int type);

    public int getItemType(int position) {
        return 0;
    }

    public abstract int getVariableId();

    public abstract int getVmVariableId();
}
