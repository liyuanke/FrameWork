package com.kunminx.architecture.ui.state.item_view_model;

import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.command.BindingAction;
import com.kunminx.architecture.command.BindingCommand;

public class ItemModel<T> extends ViewModel {
    private T value;

    public BindingCommand<BindingAction> bindingCommand;

    public ItemModel(BindingAction action) {
        this.bindingCommand = new BindingCommand<>(action);
    }

    public ItemModel setAction(BindingAction action) {
        this.bindingCommand = new BindingCommand<>(action);
        return this;
    }

    public ItemModel(T value, BindingAction action) {
        this.value = value;
        this.setAction(action);
    }

    public ItemModel(T data) {
        this.value = data;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
