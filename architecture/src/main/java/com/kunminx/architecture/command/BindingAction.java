package com.kunminx.architecture.command;

import android.view.View;

/**
 * A zero-argument action.
 */

public interface BindingAction {
    void call(View view,int position);
}
