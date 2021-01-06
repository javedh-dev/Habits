/*
 * Copyright (C) 2020 - 2021 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/*
 * @created on 06 Jan, 2021 07:39 AM
 * @project - Habits
 * @author - javed
 */
public class HabitsSwipeRefreshLayout extends SwipeRefreshLayout {
    private ViewGroup container;

    public HabitsSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public HabitsSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        // The swipe refresh layout has 2 children; the circle refresh indicator
        // and the view container. The container is needed here
        ViewGroup container = getContainer();
        if (container == null) {
            return false;
        }

        // The container has 2 children; the empty view and the scrollable view.
        // Use whichever one is visible and test that it can scroll
        View view = container.getChildAt(0);
        if (view.getVisibility() != View.VISIBLE) {
            view = container.getChildAt(1);
        }

        return view.canScrollVertically(-1);
    }

    private ViewGroup getContainer() {
        // Cache this view
        if (container != null) {
            return container;
        }

        // The container may not be the first view. Need to iterate to find it
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ViewGroup) {
                container = (ViewGroup) getChildAt(i);

                if (container.getChildCount() != 2) {
                    throw new RuntimeException("Container must have an empty view and content view");
                }

                break;
            }
        }

        if (container == null) {
            throw new RuntimeException("Container view not found");
        }

        return container;
    }
}
