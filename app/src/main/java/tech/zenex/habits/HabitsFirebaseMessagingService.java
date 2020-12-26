/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

// TODO: 12-12-2020 Clean this class to add support for notifications

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class HabitsFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Log.d("From", String.valueOf(remoteMessage.getFrom()));
//        Log.d("To", String.valueOf(remoteMessage.getTo()));
//        Log.d("Sent Time", String.valueOf(remoteMessage.getSentTime()));
//        Log.d("Message ID", String.valueOf(remoteMessage.getMessageId()));
//        Log.d("Message Type", String.valueOf(remoteMessage.getMessageType()));
//        Log.d("Sender ID", String.valueOf(remoteMessage.getSenderId()));
//        Log.d("Data", String.valueOf(remoteMessage.getData()));
//        Log.d("Collapse Key", String.valueOf(remoteMessage.getCollapseKey()));
//        Log.d("Priority", String.valueOf(remoteMessage.getPriority()));
        String title = String.valueOf(Objects.requireNonNull(remoteMessage.getNotification()).getTitle());
        String desc = String.valueOf(Objects.requireNonNull(remoteMessage.getNotification()).getBody());
        String url = String.valueOf(remoteMessage.getData().get("url"));
//        Log.d("Title", title);
//        Log.d("Description", desc);
        sendNotification(title, desc, url);
    }

    private void sendNotification(String title, String desc, String url) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "demo")
                        .setSmallIcon(R.drawable.ic_icon)
                        .setContentTitle(title)
                        .setContentText(desc)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .addAction(R.drawable.ic_icon, "Download", contentIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Habits",
                    "Take control of Life",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
