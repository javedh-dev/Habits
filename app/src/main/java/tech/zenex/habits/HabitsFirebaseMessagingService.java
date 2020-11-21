/*
 * Copyright (c) 2020.  Zenex.Tech@ https://zenex.tech
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.zenex.habits;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class HabitsFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Firebase Message";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("From", String.valueOf(remoteMessage.getFrom()));
        Log.d("To", String.valueOf(remoteMessage.getTo()));
        Log.d("Sent Time", String.valueOf(remoteMessage.getSentTime()));
        Log.d("Message ID", String.valueOf(remoteMessage.getMessageId()));
        Log.d("Message Type", String.valueOf(remoteMessage.getMessageType()));
        Log.d("Sender ID", String.valueOf(remoteMessage.getSenderId()));
        Log.d("Data", String.valueOf(remoteMessage.getData()));
        Log.d("Collapse Key", String.valueOf(remoteMessage.getCollapseKey()));
        Log.d("Priority", String.valueOf(remoteMessage.getPriority()));
        String title = String.valueOf(Objects.requireNonNull(remoteMessage.getNotification()).getTitle());
        String desc = String.valueOf(Objects.requireNonNull(remoteMessage.getNotification()).getBody());
        String url = String.valueOf(remoteMessage.getData().get("url"));
        Log.d("Title", title);
        Log.d("Description", desc);
        sendNotification(title, desc, url);
    }

    private void sendNotification(String title, String desc, String url) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);
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
            NotificationChannel channel = new NotificationChannel("demo",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("New Token", s);
    }
}
