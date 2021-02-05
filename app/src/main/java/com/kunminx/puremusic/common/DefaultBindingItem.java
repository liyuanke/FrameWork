package com.kunminx.puremusic.common;

import com.kunminx.architecture.command.BindingItem;
import com.kunminx.strictdatabinding.BR;

public abstract class DefaultBindingItem<T> extends BindingItem {

    @Override
    public int getVariableId() {
        return BR.data;
    }

    @Override
    public int getVmVariableId() {
        return BR.vm;
    }
}
