/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Collections;
import java.util.Map;

import dev.javed.habits.fragments.SettingsFragment;
import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;
import dev.javed.habits.utils.HabitsLoadingDialog;
import dev.javed.habits.utils.TaskCompletionListener;

public class SettingsActivity extends AppCompatActivity {

    private final int RC_SIGN_IN = 1001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private HabitsLoadingDialog loader;
    private TaskCompletionListener<Map<String, Boolean>> preferenceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Settings");
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        loader = new HabitsLoadingDialog(this, null);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void verifyLogin() {
        loader.show();
        mAuth = FirebaseAuth.getInstance();
        mAuth.getAccessToken(true).addOnCompleteListener(tokenTask -> {
            if (!tokenTask.isSuccessful()) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                signIn();
            } else {

                loader.dismiss();
                sendPreferenceResult(true);
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                } else {
                    sendPreferenceResult(false);
                    loader.dismiss();
                }
            } catch (ApiException e) {
                sendPreferenceResult(false);
                loader.dismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        sendPreferenceResult(true);
                    } else {
                        HabitsBasicUtil.notifyUser(getApplicationContext(),
                                R.string.authentication_failed_message);
                        sendPreferenceResult(false);
                    }
                    loader.dismiss();
                });
    }

    private void sendPreferenceResult(boolean isSuccessful) {
        if (this.preferenceListener != null)
            this.preferenceListener.onTaskCompleted(
                    Collections.singletonMap(HabitsConstants.PREFERENCE_BACKUP, isSuccessful));
    }

    public void registerPreferenceListener(TaskCompletionListener<Map<String, Boolean>> listener) {
        this.preferenceListener = listener;
    }

}