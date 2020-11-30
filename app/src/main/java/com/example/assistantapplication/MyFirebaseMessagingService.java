package com.example.assistantapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FMS";
    public MyFirebaseMessagingService(){

    }

    @Override
    public void onNewToken(String token){
        super.onNewToken(token);
        Log.e(TAG, "onNewToken 호출됨: " + token);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> map = remoteMessage.getData();
        String title = map.get("title");
        String msg = map.get("body");
        String channelId = "channel";
        String channelName = "channel name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            mBuilder = new NotificationCompat.Builder(this, channelId);
        }
        else{
            mBuilder = new NotificationCompat.Builder(this);
        }

        Intent intent = new Intent(this, LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 101, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setSmallIcon(R.drawable.logowithwhite);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(msg);
        mBuilder.setAutoCancel(true);
        mBuilder.setVibrate(new long[]{2,500});
        mBuilder.setContentIntent(contentIntent);

        notificationManager.notify(0,mBuilder.build());

    }
}
