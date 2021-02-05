/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.architecture.utils;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

/**
 * Create by KunMinX at 2020/7/19
 */
public class RvDiffUtils<T> {

    private DiffUtil.ItemCallback<T> mTestMusicItemCallback;

    private RvDiffUtils() {
    }

    private static final RvDiffUtils S_DIFF_UTILS = new RvDiffUtils();

    public static RvDiffUtils getInstance() {
        return S_DIFF_UTILS;
    }

    public DiffUtil.ItemCallback getItemCallback() {
        if (mTestMusicItemCallback == null) {
            mTestMusicItemCallback = new DiffUtil.ItemCallback<T>() {
                @Override
                public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                    return oldItem == newItem;
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                    return oldItem.equals(newItem);
                }
            };
        }
        return mTestMusicItemCallback;
    }
}
