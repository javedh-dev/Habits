/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.fragments;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import tech.zenex.habits.R;

/*
 * @created on 19 Dec, 2020 02:04 PM
 * @project - Habits
 * @author - javed
 */
public class HabitsBottomSheet extends BottomSheetDialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        View view = getView();
        if (view != null) {
            view.post(() -> {
                View parent = (View) view.getParent();
                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(parent);
                bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
            });
        }
    }
}
