package com.example.android.recappe.PushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.example.android.recappe.ViewStoredFoods.MainActivity_viewstoredfooditems;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.android.recappe.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //print out message title and body for debugging purposes
            Log.d("Firebase", "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d("Firebase", "Message Notification Body: " + remoteMessage.getNotification().getBody());


            // Create intent that will be executed when user presses notification
            Intent intent = new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            //create notification that will be displayed to user
            Notification notification = new Notification.Builder(getApplicationContext(), "0")
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();

            //set up notification channel - only needed for newer os versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String name = "My notification";
                String description = "My notification description";
                int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
                NotificationChannel channel = new NotificationChannel("0", name, importance);
                channel.setDescription(description);
                channel.setShowBadge(true);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            //creating notification manager object to be able to show notification
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //showing notification - using a timestamp as a unique id
            nm.notify((int) System.currentTimeMillis(), notification);
        }
    }
}
