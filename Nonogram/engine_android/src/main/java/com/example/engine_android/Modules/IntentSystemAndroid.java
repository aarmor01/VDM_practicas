package com.example.engine_android.Modules;

import static android.provider.Settings.System.getString;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class IntentSystemAndroid {
    // activity context
    private Context context;
    private Activity activity;
    private String channel_name;
    private String channel_description;
    private String channel_id;

    public IntentSystemAndroid(Context cont) {
        this.context = cont;
        this.channel_name = "Pruebas";
        this.channel_description = "Esto es un canal de Prueba";
        this.channel_id = "nonogram_prueba";

        createChannel();
    }

    private void createChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O) {
            int importance = NotificationManager. IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(this.channel_id , this.channel_name, importance) ;
            channel.setDescription(this.channel_description) ;
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.context.getSystemService(NotificationManager. class);
            notificationManager.createNotificationChannel(channel) ;
        }
    }

    public void createNotification(int icon) {
        //Intent intent = new Intent(context , MiClase.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, this.channel_id)
                .setSmallIcon(icon)
                .setContentTitle( "My notification" )
                .setContentText( "Much longer text that cannot fit one line..." )
                .setStyle( new NotificationCompat.BigTextStyle()
                        .bigText( "Much longer text that cannot fit one line..." ))
                .setPriority(NotificationCompat. PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());
    }
}