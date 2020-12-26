/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import tech.zenex.habits.R;

public class HabitsLoadingDialog extends Dialog {
    private final String message;

    public HabitsLoadingDialog(@NonNull Context context, String message) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        if (message != null)
            this.message = message;
        else
            this.message = HabitsConstants.EMPTY_STRING;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader_fragment);
        TextView messageView = findViewById(R.id.message_view);
        messageView.setText(message);
    }
}
