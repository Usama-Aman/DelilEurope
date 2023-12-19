package com.dalileuropeapps.dalileurope.notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.activities.ChatActivity;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.dalileuropeapps.dalileurope.activities.ChatActivity.isActivityOn;
import static com.dalileuropeapps.dalileurope.fragments.MessagesListFragment.isFragmentActive;


@SuppressLint("Registered")
public class FCMService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("Firebase New Token", "onNewToken: ------------" + s);

        String deviceToken = s;
        // Saving reg id to shared preferences
        SharedPreference.saveSharedPrefValue(this, NotificationConfig.FCM_ID, deviceToken);

    }

    private static void sendMessageToActivity(Context context, String msg) {
        Intent intent = new Intent("GPSLocationUpdates");
        // You can also include some extra data.
        intent.putExtra("Status", "true");

        intent.putExtra("message_obj", msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private static void sendMessageToFragment(Context context, String msg) {
        Intent intent = new Intent("threadupdates");
        // You can also include some extra data.
        intent.putExtra("Status", "true");

        intent.putExtra("thread_obj", msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    //Function called when new message is received

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        String title = null, body = null, orderId = null, type = null, price = null, quantity = null;
        String notifi, user_id, subject, sender_name, id, status, message_obj;
        int thread_id;
        //Toast.makeText(this, "Received:", Toast.LENGTH_LONG).show();
        Log.e(TAG, "Notification Body:" + remoteMessage.getData());
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body:" + remoteMessage.getNotification().getBody());

        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());


            try {

                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(remoteMessage.getData().toString());

                JSONObject data = object.getJSONObject("notification");
                JSONObject obj = data.getJSONObject("obj");
                notifi = obj.getString("notification");
                thread_id = obj.getInt("thread_id");
                user_id = obj.getString("user_id");
                Log.e(TAG, "Sender Id " + obj.getJSONObject("message_obj").getString("from_user_id"));
                subject = obj.getString("subject");
                sender_name = obj.getString("sender_name");
                id = obj.getString("id");
                status = obj.getString("status");
                message_obj = obj.getString("message_obj");

                Log.e(TAG, "message_objString: " + message_obj);
                if (isActivityOn) {

                    sendMessageToActivity(getApplicationContext(), message_obj);
                } else if (!isActivityOn) {
                    user_id = obj.getJSONObject("message_obj").getString("from_user_id");
                    if (isFragmentActive) {
                        JSONObject messagethread = obj.getJSONObject("message_obj").getJSONObject("message_thread");
                        if (messagethread != null)
                            sendMessageToFragment(getApplicationContext(), messagethread.toString());
                    }
                    showMessage(notifi, thread_id, sender_name, user_id);
                } else {
                    showMessage(notifi, thread_id, sender_name, user_id);
                }


            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }


    }

    //"Showing the notification
    private void showMessage(String notifi, int thread_id, String sender_name, String user_id) {

        Intent resultIntent;
        resultIntent = new Intent(this, ChatActivity.class);
        resultIntent.putExtra("thread_id", thread_id);
        resultIntent.putExtra("name", sender_name);
        resultIntent.putExtra("to_user_id", user_id);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("Channel 1", "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // to display notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel("Channel 1");
            channel.canBypassDnd();
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Channel 1");

        notificationBuilder
                .setContentTitle(getResources().getString(R.string.new_message))
                .setContentText(notifi)
                .setGroup("Channel 1")
                .setStyle(new NotificationCompat.BigTextStyle())
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.noti)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationChannel != null) {
                notificationBuilder.setChannelId("Channel 1");
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
        }

        mNotificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }



}
