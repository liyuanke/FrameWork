package com.kunminx.architecture.command;


import android.view.View;

/**
 * About : kelin的ReplyCommand
 * 执行的命令回调, 用于ViewModel与xml之间的数据绑定
 */
public class BindingCommand<T extends BindingAction> {
    private T execute;

    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public BindingCommand(T execute) {
        this.execute = execute;
    }

    public T getAction() {
        return this.execute;
    }

    /**
     * 执行BindingAction命令
     */
    public void execute(View view) {
        if (execute != null) {
            execute.call(view, position);
        }
    }

}
