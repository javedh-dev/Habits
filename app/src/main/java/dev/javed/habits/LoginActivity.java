/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import dev.javed.habits.utils.HabitsBasicUtil;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && checkBiometricSupport()) {
            authenticate();
        } else {
            new Handler().postDelayed(this::openMainActivity, 1000);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void authenticate() {
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Fingerprint Authentication")
                .setNegativeButton("Cancel", getMainExecutor(), (dialog, which) -> {

                }).build();

        biometricPrompt.authenticate(getCancellationSignal(), getMainExecutor(),
                getAuthenticationCallback());
    }

    private Boolean checkBiometricSupport() {

        KeyguardManager keyguardManager =
                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        PackageManager packageManager = this.getPackageManager();

        if (!keyguardManager.isKeyguardSecure()) {
            HabitsBasicUtil.notifyUser(getApplicationContext(), R.string.fingerprint_not_enabled_message);
            return false;
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.USE_BIOMETRIC) !=
                PackageManager.PERMISSION_GRANTED) {

            HabitsBasicUtil.notifyUser(getApplicationContext(),
                    R.string.fingerprint_permission_not_enabled_message);
            return false;
        }
        return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {

        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              CharSequence errString) {
                HabitsBasicUtil.notifyUser(getApplicationContext(), R.string.authentication_error_message);
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode,
                                             CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                HabitsBasicUtil.notifyUser(getApplicationContext(),
                        R.string.authentication_failed_message);
                authenticate();
            }

            @Override
            public void onAuthenticationSucceeded(
                    BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                openMainActivity();
            }
        };
    }

    private CancellationSignal getCancellationSignal() {
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(() -> HabitsBasicUtil.notifyUser(getApplicationContext(),
                R.string.cancelled_with_signal_message));
        return cancellationSignal;
    }

    private void openMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}