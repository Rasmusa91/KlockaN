package com.superteam.klockan;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by oornmyr on 1/3/17.
 */

public class AlarmService extends Service {

    private final static int EVENT_UNKNOWN = 0;
    public final static int EVENT_START_SERVICE = 1, EVENT_ALARM = 2;
    public final static String INTENT_EVENT_KEY = "event";

    private boolean alarmsInitialized = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("AlarmService", "OnCreate()");
    }

    private void initAlarms(){
        if(alarmsInitialized == false){
            //TODO Check preferences for alarms to be set.
            Toast.makeText(this, "Alarms initialized", Toast.LENGTH_SHORT).show();
            Log.e("AlarmService", "Alarms initialized");

            alarmsInitialized = true;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            int eventFlag = intent.getIntExtra(INTENT_EVENT_KEY, EVENT_UNKNOWN);
            switch (eventFlag){
                case EVENT_START_SERVICE:
                    Log.e("AlarmService", "OnStartCommand - Start service event");
                    initAlarms();
                    break;
                case EVENT_ALARM:
                    Log.e("AlarmService", "OnStartCommand - Alarm event");
                    onAlarmReceived(intent);
                    break;
                case EVENT_UNKNOWN:
                    Log.e("AlarmService", "OnStartCommand - Unknown event");
                    break;
            }
        }
        return Service.START_STICKY;
    }

    private void onAlarmReceived (Intent intent){
        String msg = intent.getStringExtra("message");
        if(msg == null){
            msg = "";
        }

        Toast.makeText(this, "Alarm: " + msg, Toast.LENGTH_SHORT).show();
        Log.e("AlarmService", "Alarm - " + msg);

        Intent editAlarmIntent = new Intent(this, EditAlarmActivity.class);
        this.startActivity(editAlarmIntent);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "AlarmService onDestroy()", Toast.LENGTH_SHORT).show();
        Log.e("AlarmService", "OnDestroy()");

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "AlarmService.onBind()", Toast.LENGTH_SHORT).show();
        Log.e("AlarmService", "OnBind()");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "AlarmService.onUnbind()", Toast.LENGTH_SHORT).show();
        Log.e("AlarmService", "OnUnBind()");
        return super.onUnbind(intent);
    }
}
