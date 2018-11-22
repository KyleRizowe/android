package com.fuel4media.carrythistoo.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;

import com.fuel4media.carrythistoo.R;
import com.fuel4media.carrythistoo.activity.DashboaredActivity;
import com.fuel4media.carrythistoo.model.AdminMessage;
import com.fuel4media.carrythistoo.model.Event;
import com.fuel4media.carrythistoo.model.Message;
import com.fuel4media.carrythistoo.model.response.GunFreeZone;
import com.fuel4media.carrythistoo.utils.CommonMethods;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    private String messageType;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("type"));

            messageType = remoteMessage.getData().get("type");

            /*  if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/

        }

        // Check if message contains a notification payload.
        //if (remoteMessage.getNotification() != null) {
        // Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        if (messageType.equals("admin_alert")) {
            AdminMessage message = new AdminMessage();

            message.setTitle(remoteMessage.getData().get("title"));
            message.setMessage(remoteMessage.getData().get("message"));
            handleNow(message);
        }

        if (messageType.equals("message")) {
            // Message message = (Message) GsonUtil.toModel(remoteMessage.getData().get("message"), Message.class);
            Message message = new Message();

            message.setMessage_type(remoteMessage.getData().get("msg_type"));
            message.setMessages(remoteMessage.getData().get("messages"));
            message.setId(Integer.parseInt(remoteMessage.getData().get("id")));
            message.setUser_id(Integer.parseInt(remoteMessage.getData().get("user_id")));
            message.setTimestamp(Long.parseLong(remoteMessage.getData().get("timestamp")));
            message.setDate(remoteMessage.getData().get("date"));
            message.setStatus(remoteMessage.getData().get("status"));

            handleNow(message);
        }

        if (messageType.equals("event")) {
            //Event event = (Event) GsonUtil.toModel(remoteMessage.getData().get("event"), Event.class);

            //handleEventNow(remoteMessage.getNotification().getBody());

            Event event = new Event();

            event.setEvent_name(remoteMessage.getData().get("event_name"));
            event.setId(remoteMessage.getData().get("id"));
            event.setDescriptions(remoteMessage.getData().get("descriptions"));
            event.setTimestamp(Long.parseLong(remoteMessage.getData().get("timestamp")));
            event.setAddlocation(remoteMessage.getData().get("addlocation"));
            event.setUser_id(remoteMessage.getData().get("user_id"));
            event.setNotify(Integer.parseInt(remoteMessage.getData().get("notify")));

            handleEventNow(event);
        }

        if (messageType.equals("gunzone")) {
            // GunFreeZone gunFreeZone = (GunFreeZone) GsonUtil.toModel(remoteMessage.getData().get("gunzone"), GunFreeZone.class);
            GunFreeZone gunFreeZone = new GunFreeZone();

            gunFreeZone.setGun_key_id(remoteMessage.getData().get("gun_key_id"));
            // gunFreeZone.setGun_subkey_id(remoteMessage.getData().get("gun_subkey_id"));
            gunFreeZone.setState(remoteMessage.getData().get("state"));
            gunFreeZone.setLat(Double.parseDouble(remoteMessage.getData().get("lat")));
            gunFreeZone.setLong(Double.parseDouble(remoteMessage.getData().get("long")));
            gunFreeZone.setDistance(Double.parseDouble(remoteMessage.getData().get("distance")));
            gunFreeZone.setZone_name(remoteMessage.getData().get("zone_name"));

            handleNow(gunFreeZone);
        }
    }

    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.
    // }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
     /*   // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);*/
        // [END dispatch_job]
    }


    private void handleNow(GunFreeZone gunFreeZone) {
        Log.d(TAG, "Short lived task is done.");

        if (DashboaredActivity.Companion.isVisible()) {
            Intent intent = new Intent("gunzone_notification");
            // You can also include some extra data.
            intent.putExtra("gunzone", gunFreeZone);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            sendGunZoneNotification(gunFreeZone);
        }
    }


    private void handleNow(AdminMessage adminMessage) {
        Log.d(TAG, "Short lived task is done.");

        if (DashboaredActivity.Companion.isVisible()) {
            Intent intent = new Intent("admin_message_notification");
            // You can also include some extra data.
            intent.putExtra("admin_message", adminMessage);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            sendAdminMessageNotification(adminMessage);
        }
    }

    private void sendAdminMessageNotification(AdminMessage adminMessage) {
        Intent intent = new Intent(this, DashboaredActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", "admin_message");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "111";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(Html.fromHtml("<b><font color='#000000'>" + adminMessage.getTitle() + "</font></b>"))
                        .setContentText(Html.fromHtml("<font color='#000000'>" + adminMessage.getMessage() + "</font>"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        // .setColor(getResources().getColor(R.color.colorPrimaryDark))
                        //.setSmallIcon(R.drawable.icon_notification)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(adminMessage.getMessage())))
                        .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification_color);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow(Message message) {
        Log.d(TAG, "Short lived task is done.");

        if (DashboaredActivity.Companion.isVisible()) {
            Intent intent = new Intent("message_notification");
            // You can also include some extra data.
            intent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            sendNotification(message);
        }
    }

    private void handleEventNow(String body) {
        Log.d(TAG, "Short lived task is done.");

        if (DashboaredActivity.Companion.isVisible()) {
            Intent intent = new Intent("event_notification");
            // You can also include some extra data.
            intent.putExtra("event", body);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            sendEventNotification(body);
        }
    }

    private void handleEventNow(Event event) {
        Log.d(TAG, "Short lived task is done.");

        if (DashboaredActivity.Companion.isVisible()) {
            Intent intent = new Intent("event_notification");
            // You can also include some extra data.
            intent.putExtra("event", event);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            sendEventNotification(event);
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(Message messageBody) {
        Intent intent = new Intent(this, DashboaredActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", "message");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "111";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(Html.fromHtml("<b><font color='#000000'>Message From Admin!</font></b>"))
                        .setContentText(Html.fromHtml("<font color='#000000'>" + messageBody.getMessages() + "</font>"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        // .setColor(getResources().getColor(R.color.colorPrimaryDark))
                        // .setSmallIcon(R.drawable.icon_notification)
                        .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification_color);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    private void sendEventNotification(String body) {
        Intent intent = new Intent(this, DashboaredActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", "event");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "111";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle("Carry This Too")
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        //.setColor(getResources().getColor(R.color.colorPrimaryDark))
                        //.setSmallIcon(R.drawable.logo)
                        .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification_color);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendEventNotification(Event event) {
        Intent intent = new Intent(this, DashboaredActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", "event");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "111";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String content = Html.fromHtml("<font color='#000000'>" + event.getEvent_name() + " Date :" + CommonMethods.Companion.changeDateFormatTOMonth(event.getTimestamp()) + " \n Location : " + event.getAddlocation() + "</font>").toString();
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(Html.fromHtml("<b><font color='#000000'>Upcoming Event Alert!</font></b>"))
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        //.setColor(getResources().getColor(R.color.colorPrimaryDark))
                        //.setSmallIcon(R.drawable.icon_notification)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                        .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification_color);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendGunZoneNotification(GunFreeZone gunFreeZone) {
        Intent intent = new Intent(this, DashboaredActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", "gunzone");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "111";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(Html.fromHtml("<b><font color='#000000'>Gun-Free Zone Alert!</font></b>"))
                        .setContentText(Html.fromHtml("<font color='#000000'>" + gunFreeZone.getGun_key_id() + " Distance - " + gunFreeZone.getDistance() + " Miles </font>"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        //.setSmallIcon(R.drawable.icon_notification)
                        //.setColor(getResources().getColor(R.color.colorPrimaryDark))
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_notification))
                        .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification_color);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
