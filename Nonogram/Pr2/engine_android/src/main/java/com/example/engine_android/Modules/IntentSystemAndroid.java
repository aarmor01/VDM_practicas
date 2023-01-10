package com.example.engine_android.Modules;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;

public class IntentSystemAndroid {

    public enum SocialNetwork {TWITTER}

    // activity context
    private Context context;
    private Activity activity;
    private ArrayList<String> channel_names;
    private ArrayList<String> channel_descriptions;
    private ArrayList<String> channel_ids;
    int numChannels;

    public IntentSystemAndroid(Context cont) {
        this.context = cont;
        this.numChannels = 0;

        //nos guardamos la informacion por posible utilidad en el futuro
        this.channel_names = new ArrayList<>();
        this.channel_descriptions = new ArrayList<>();
        this.channel_ids = new ArrayList<>();
    }

    public void createChannel(String channel_name, String channel_descriptions, String channel_id) {
        this.channel_names.add(channel_name);
        this.channel_descriptions.add(channel_descriptions);
        this.channel_ids.add(channel_id);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O) {
            int importance = NotificationManager. IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id , channel_name, importance) ;
            channel.setDescription(channel_descriptions) ;
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.context.getSystemService(NotificationManager. class);
            notificationManager.createNotificationChannel(channel) ;
        }
        this.numChannels++;
    }

    public void share(SocialNetwork socialNetwork, String msg) {
        Intent intent;
        PackageManager packageManager = context.getPackageManager();

        switch (socialNetwork) {
            case TWITTER:
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo("com.twitter.android", packageManager.GET_META_DATA);
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName("com.twitter.android", "com.twitter.android.PostActivity");
                    intent.setType("text/*");
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
                } catch (PackageManager.NameNotFoundException e) {
                    String tweetUrl = "https://twitter.com/intent/tweet?text=" + msg;
                    Uri uri = Uri.parse(tweetUrl);
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                }
                break;
            default:
                return;
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
    }
}
