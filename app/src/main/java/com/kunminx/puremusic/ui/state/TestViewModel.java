package com.kunminx.puremusic.ui.state;

import android.view.View;

import com.kunminx.architecture.command.BindingAction;
import com.kunminx.architecture.command.BindingCommand;
import com.kunminx.architecture.ui.adapter.DefaultBindingAdapter;
import com.kunminx.architecture.ui.state.BaseViewModel;
import com.kunminx.architecture.ui.state.item_view_model.ItemModel;
import com.kunminx.architecture.utils.ToastUtils;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.common.DefaultBindingItem;
import com.kunminx.puremusic.data.bean.User;
import com.kunminx.puremusic.domain.request.UserRequest;

import java.util.LinkedList;
import java.util.List;

public class TestViewModel extends BaseViewModel {
    public UserRequest userRequest = new UserRequest(netLoadChangeObservable);
    int count = 0;
    public BindingCommand<BindingAction> bindingCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call(View view, int position) {
            ToastUtils.showShort(position + "");
            if (data.size() == 0) {
                data.add(new ItemModel("xxxx" + count++, this));
            } else {
                data.add(new ItemModel(new User("aa", "xx"), this));
            }
            adapter.setItems(data);
        }
    });

    public DefaultBindingAdapter<Object> adapter = new DefaultBindingAdapter();

    public DefaultBindingItem<ItemModel> bindingItem = new DefaultBindingItem<ItemModel>() {
        @Override
        public int getLayoutId(int type) {
            if (type == 0) {
                return R.layout.layout_test_item;
            }
            return R.layout.layout_test_item2;
        }

        @Override
        public int getItemType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public int getVariableId() {
            return BR.data;
        }
    };
    public List<ItemModel<Object>> data = new LinkedList<>();

}
