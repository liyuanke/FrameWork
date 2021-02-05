package com.kunminx.architecture.op;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int hSpacePx;//水平间隔单位px
    private int vSpacePx;//垂直间隔单位px
    private int spanCount;//列数
    private boolean firstRowEnable = true;//第一列是否显示间距
    private int spNum = -1;//前多少个有间距

    public int getSpNum() {
        return spNum;
    }

    public void setSpNum(int spNum) {
        this.spNum = spNum;
    }

    public void setFirstRowEnable(boolean firstRowEnable) {
        this.firstRowEnable = firstRowEnable;
    }

    public SpacesItemDecoration(int spacePx) {
        this(spacePx, spacePx, 1);
    }

    public SpacesItemDecoration(int spacePx, int spanCount) {
        this(spacePx, spacePx, spanCount);
    }

    public SpacesItemDecoration(int hSpacePx, int vSpacePx, int spanCount) {
        this.hSpacePx = hSpacePx;
        this.vSpacePx = vSpacePx;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int index = parent.getChildAdapterPosition(view);
        if (spanCount > 1) {
            if (index % spanCount == 0) {//最左边一列
                outRect.right = hSpacePx / 2;
                outRect.left = 0;
            } else if (index % spanCount == (spanCount - 1)) {//最右边一列
                outRect.right = 0;
                outRect.left = hSpacePx / 2;
            } else {
                outRect.right = hSpacePx / 2;
                outRect.left = hSpacePx / 2;
            }
        }
        if (spNum == -1 || spNum > index) {
            outRect.bottom = vSpacePx;
        }
        if (firstRowEnable) {
            // 设置顶部间距
            if (index < spanCount) {
                outRect.top = vSpacePx;
            }
        }
    }
}
