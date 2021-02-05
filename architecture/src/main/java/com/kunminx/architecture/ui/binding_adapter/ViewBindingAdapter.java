package com.kunminx.architecture.ui.binding_adapter;

import android.view.View;

import androidx.databinding.BindingAdapter;

import com.kunminx.architecture.command.BindingCommand;

public class ViewBindingAdapter {
    /**
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     */
    @BindingAdapter(value = {"onClickCommand"}, requireAll = false)
    public static void onClickCommand(View view, final BindingCommand clickCommand) {
        if (clickCommand != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCommand.execute(view);
                }
            });
        }
    }
}
