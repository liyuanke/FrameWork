package com.kunminx.architecture.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.kunminx.architecture.command.BindingItem;
import com.kunminx.architecture.ui.state.item_view_model.ItemModel;

import java.util.LinkedList;
import java.util.List;

public class DefaultBindingAdapter<T> extends RecyclerView.Adapter<DefaultBindingAdapter.BaseBindingViewHolder> {

    protected OnItemClickListener<ItemModel<T>> mOnItemClickListener;
    protected OnItemLongClickListener<ItemModel<T>> mOnItemLongClickListener;
    private List<ItemModel<T>> items;

    public void setOnItemClickListener(OnItemClickListener<ItemModel<T>> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<ItemModel<T>> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setItems(@Nullable List<ItemModel<T>> items) {
        if (this.items == null) {
            this.items = new LinkedList<>(items);
        } else {
            this.items.clear();
            this.items.addAll(items);
        }
        notifyDataSetChanged();
    }

    public ItemModel<T> getItem(int position) {
        if (this.items != null && position < this.items.size()) {
            return this.items.get(position);
        }
        return null;
    }

    @Override
    @NonNull
    public DefaultBindingAdapter.BaseBindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), this.getLayoutResId(viewType), parent, false);
        BaseBindingViewHolder holder = new BaseBindingViewHolder(binding.getRoot());
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = holder.getBindingAdapterPosition();
                mOnItemClickListener.onItemClick(holder.itemView.getId(), getItem(position), position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnItemLongClickListener != null) {
                int position = holder.getBindingAdapterPosition();
                mOnItemLongClickListener.onItemLongClick(holder.itemView.getId(), getItem(position), position);
                return true;
            }
            return false;
        });
        return holder;
    }

    protected BindingItem bindingItem;

    public void setBindingItem(BindingItem bindingItem) {
        this.bindingItem = bindingItem;
    }

    @Override
    public void onBindViewHolder(DefaultBindingAdapter.BaseBindingViewHolder holder, final int position) {
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        ItemModel itemModel = getItem(position);
        itemModel.bindingCommand.setPosition(position);
        binding.setVariable(bindingItem.getVmVariableId(), itemModel);
        binding.setVariable(bindingItem.getVariableId(), itemModel.getValue());
        if (binding != null) {
            binding.executePendingBindings();
        }
    }

    @LayoutRes
    protected int getLayoutResId(int viewType) {
        if (bindingItem != null) {
            return bindingItem.getLayoutId(viewType);
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (bindingItem != null) {
            return bindingItem.getItemType(position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return this.items == null ? 0 : this.items.size();
    }

    public static class BaseBindingViewHolder extends RecyclerView.ViewHolder {
        public BaseBindingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener<M> {
        void onItemClick(int viewId, M item, int position);
    }

    public interface OnItemLongClickListener<M> {
        void onItemLongClick(int viewId, M item, int position);
    }
}