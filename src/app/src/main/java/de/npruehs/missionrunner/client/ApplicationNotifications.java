package de.npruehs.missionrunner.client;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApplicationNotifications {
    private static final String CHANNEL_ID_MISSIONS = "Missions";

    private static final int NOTIFICATION_ID_MISSION_FINISHED = 1;

    private final Application application;

    private final HashMap<Integer, CountDownTimer> notificationTimers = new HashMap<>();

    @Inject
    public ApplicationNotifications(Application application) {
        this.application = application;

        addChannel(CHANNEL_ID_MISSIONS,
                R.string.notification_channel_missions_name,
                R.string.notification_channel_missions_description);
    }

    public void addMissionFinishedNotification(int seconds, int missionId) {
        HashMap<String, Object> intentExtras = new HashMap<>();
        intentExtras.put(MainActivity.EXTRA_MISSIONID, missionId);

        addNotification(CHANNEL_ID_MISSIONS, NOTIFICATION_ID_MISSION_FINISHED,
                R.string.notification_missionfinished_title,
                R.string.notification_missionfinished_text,
                seconds,
                intentExtras);
    }

    private void addChannel(String channelId, int nameResource, int descriptionResource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create notification channel.
            String name = application.getString(nameResource);
            String description = application.getString(descriptionResource);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = application.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void addNotification(final String channelId, final int notificationId,
                                 final int notificationTitleResource,
                                 final int notificationDescriptionResource, int seconds,
                                 final HashMap<String, Object> intentExtras) {
        // Cancel existing timers.
        if (notificationTimers.containsKey(notificationId)) {
            notificationTimers.get(notificationId).cancel();
        }

        CountDownTimer countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Nothing to do.
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(application, MainActivity.class);

                if (intentExtras != null) {
                    for (String extraKey : intentExtras.keySet()) {
                        Object extraValue = intentExtras.get(extraKey);

                        if (extraValue instanceof Integer) {
                            Integer extraInteger = (Integer)extraValue;
                            intent.putExtra(extraKey, extraInteger);
                        } else {
                            throw new UnsupportedOperationException("Notification intent extra type not supported: " + extraValue.getClass().toString());
                        }
                    }
                }

                PendingIntent pendingIntent = PendingIntent.getActivity(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(application, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(application.getString(notificationTitleResource))
                        .setContentText(application.getString(notificationDescriptionResource))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(application);

                notificationManager.notify(notificationId, builder.build());
            }
        };
        countDownTimer.start();

        notificationTimers.put(notificationId, countDownTimer);
    }
}
