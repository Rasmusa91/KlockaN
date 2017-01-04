package com.superteam.klockan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by oornmyr on 1/3/17.
 */

public class AlarmService extends Service {

    private final static int EVENT_UNKNOWN = 0;
    public final static int EVENT_START_SERVICE = 1, EVENT_ALARM = 2, EVENT_SET_ALARMS = 3;
    public final static String INTENT_EVENT_KEY = "event";

    private boolean alarmsInitialized = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("AlarmService", "OnCreate()");
        initAlarms(false);
    }

    private void initAlarms(boolean forceUpdate){
        if(alarmsInitialized == false || forceUpdate == true){

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            ArrayList<AlarmObject> alarmList = Preferences.getAllAlarms(getApplicationContext());
            for (AlarmObject alarm : alarmList) {
                Intent alarmIntent = new Intent(this, AlarmService.class);
                alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                alarmIntent.putExtra("message", alarm.getTitle());
                alarmIntent.putExtra("alarm_id", alarm.getID());
                alarmIntent.putExtra(AlarmService.INTENT_EVENT_KEY, AlarmService.EVENT_ALARM);

                PendingIntent pi = PendingIntent.getService(this, alarm.getID(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pi);

                if(alarm.isEnabled()){
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMS(), pi);
                }
            }

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
                    initAlarms(false);
                    break;
                case EVENT_ALARM:
                    Log.e("AlarmService", "OnStartCommand - Alarm event");
                    onAlarmReceived(intent);
                    break;
                case EVENT_SET_ALARMS:
                    initAlarms(true);
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
        editAlarmIntent.putExtra("editObjectId", intent.getExtras().getInt("alarm_id"));
        editAlarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        TaskStackBuilder.create(this).addNextIntentWithParentStack(editAlarmIntent).startActivities();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "AlarmService onDestroy()", Toast.LENGTH_SHORT).show();
        Log.e("AlarmService", "OnDestroy()");

        alarmsInitialized = false;
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
