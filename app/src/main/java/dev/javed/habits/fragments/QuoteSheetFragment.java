/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

import dev.javed.habits.R;
import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;

public class QuoteSheetFragment extends DialogFragment {

    private CardView cardView;

    public QuoteSheetFragment() {
    }

    public static QuoteSheetFragment newInstance(String author, String quote) {
        Bundle args = new Bundle();
        args.putString(HabitsConstants.ARGS_QUOTE_KEY, quote);
        args.putString(HabitsConstants.ARGS_AUTHOR_KEY, author);
        QuoteSheetFragment f = new QuoteSheetFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*public static QuoteSheetFragment newInstance(HabitEntity habitEntity) {
        Bundle args = new Bundle();
        args.putSerializable(HabitsConstants.ARGS_HABITS_ENTITY_KEY, habitEntity);
        QuoteSheetFragment f = new QuoteSheetFragment();
        f.setArguments(args);
        return f;
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quote_card, container, false);
        FloatingActionButton share = view.findViewById(R.id.share_button);
        FloatingActionButton theme = view.findViewById(R.id.theme_button);
        setupQuote(view);
        cardView = view.findViewById(R.id.card);
        share.setOnClickListener(v -> {
            Bitmap bitmap = HabitsBasicUtil.getBitmapFromView(cardView);
            shareBitmap(bitmap);
        });
        theme.setOnClickListener(v -> cardView.setCardBackgroundColor(HabitsBasicUtil.getRandomCardColor(requireContext())));
        return view;
    }

    private void setupQuote(View view) {
        if (getArguments() != null) {
            TextView author = view.findViewById(R.id.author);
            TextView quote = view.findViewById(R.id.quote_text);
            author.setText(getArguments().getString(HabitsConstants.ARGS_AUTHOR_KEY));
            quote.setText(getArguments().getString(HabitsConstants.ARGS_QUOTE_KEY));
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
//        setCancelable(false);
    }

    /*private void checkInDatabase(int checkedId, boolean isChecked) {
        HabitEntity habitEntity = getHabitEntity();
        if (habitEntity != null) {
            HabitEntity.HabitType checkInStatus = isCheckInStatus(checkedId, isChecked, habitEntity);
            HabitTrackerEntity tracker = new HabitTrackerEntity(habitEntity.getHabitID(),
                    checkInStatus);
            HabitsRepository.addHabitTracker(tracker, getContext(), result -> {
                if (result.isSuccessful()) {
                    Objects.requireNonNull(getDialog()).dismiss();
                } else {
                    HabitsBasicUtil.notifyUser(requireActivity(), result.getMessage());
                }
            });
        } else {
            HabitsBasicUtil.notifyUser(getContext(), R.string.check_in_failed_message);
        }
    }*/

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void shareBitmap(@NonNull Bitmap bitmap) {
        //---Save bitmap to external cache directory---//
        //get cache directory
        File cachePath = new File(requireActivity().getExternalCacheDir(), "quotes/");
        cachePath.mkdirs();

        //create png file
        File file = new File(cachePath, "Image_123.png");
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //---Share File---//
        //get file uri
        Uri myImageFileUri = FileProvider.getUriForFile(requireContext(),
                requireContext().getPackageName() + ".provider", file);

        //create a intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri);
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, "Share with"));
    }
}
