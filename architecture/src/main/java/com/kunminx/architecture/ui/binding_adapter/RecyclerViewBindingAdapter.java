package com.kunminx.architecture.ui.binding_adapter;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kunminx.architecture.command.BindingAction;
import com.kunminx.architecture.command.BindingCommand;
import com.kunminx.architecture.command.BindingItem;
import com.kunminx.architecture.ui.adapter.DefaultBindingAdapter;

public class RecyclerViewBindingAdapter {
    @SuppressWarnings("unchecked")
    @BindingAdapter({"onloadMoreCommand"})
    public static void onLoadMoreCommand(final RecyclerView recyclerView, final BindingCommand<BindingAction> onLoadMoreCommand) {
        RecyclerView.OnScrollListener listener = new OnScrollListener(onLoadMoreCommand);
        recyclerView.addOnScrollListener(listener);
    }

    @BindingAdapter({"layoutManager"})
    public static void layoutManager(final RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    @BindingAdapter({"layoutManager_h"})
    public static void layoutManagerH(final RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"bindingItems"})
    public static void bindingItems(final RecyclerView recyclerView, final BindingItem bindingItem) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter instanceof DefaultBindingAdapter) {
            ((DefaultBindingAdapter) adapter).setBindingItem(bindingItem);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"onItemClickListener"})
    public static void setOnItemClickListener(final RecyclerView recyclerView, final DefaultBindingAdapter.OnItemClickListener onItemClickListener) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter instanceof DefaultBindingAdapter) {
            ((DefaultBindingAdapter) adapter).setOnItemClickListener(onItemClickListener);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"onItemLongClickListener"})
    public static void setOnItemLongClickListener(final RecyclerView recyclerView, final DefaultBindingAdapter.OnItemLongClickListener onItemLongClickListener) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter instanceof DefaultBindingAdapter) {
            ((DefaultBindingAdapter) adapter).setOnItemLongClickListener(onItemLongClickListener);
        }
    }

    public static class OnScrollListener extends RecyclerView.OnScrollListener {
        private BindingCommand<BindingAction> onLoadMoreCommand;

        public OnScrollListener(final BindingCommand<BindingAction> onLoadMoreCommand) {
            this.onLoadMoreCommand = onLoadMoreCommand;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                int visibleItemCount = manager.getChildCount();
                int totalItemCount = manager.getItemCount();
                int pastVisiblesItems = manager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    if (onLoadMoreCommand != null) {
                        onLoadMoreCommand.execute(recyclerView);
                    }
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

    }
}
