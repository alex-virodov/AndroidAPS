package info.nightscout.androidaps.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.os.IBinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import info.nightscout.androidaps.MainApp;
import info.nightscout.androidaps.R;
import info.nightscout.androidaps.logging.L;
import info.nightscout.androidaps.plugins.general.persistentNotification.PersistentNotificationPlugin;

public class AlarmSoundService extends Service {
    private static Logger log = LoggerFactory.getLogger(L.CORE);

    int resourceId = R.raw.error;

    public AlarmSoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (L.isEnabled(L.CORE))
            log.debug("onCreate");
        Notification notification = PersistentNotificationPlugin.getPlugin().getLastNotification();
        startForeground(PersistentNotificationPlugin.ONGOING_NOTIFICATION_ID, notification);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = PersistentNotificationPlugin.getPlugin().getLastNotification();
        startForeground(PersistentNotificationPlugin.ONGOING_NOTIFICATION_ID, notification);
        if (L.isEnabled(L.CORE))
            log.debug("onStartCommand");
        if (intent != null && intent.hasExtra("soundid"))
            resourceId = intent.getIntExtra("soundid", R.raw.error);

        try {
            AssetFileDescriptor afd = MainApp.sResources.openRawResourceFd(resourceId);
            if (afd == null)
                return START_STICKY;
            afd.close();
        } catch (Exception e) {
            log.error("Unhandled exception", e);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (L.isEnabled(L.CORE))
            log.debug("onDestroy");
    }
}
