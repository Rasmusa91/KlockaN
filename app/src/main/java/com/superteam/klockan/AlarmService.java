package com.superteam.klockan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;

import java.util.ArrayList;

/**
 * Created by oornmyr on 1/3/17.
 */

public class AlarmService extends Service {

    private final static int EVENT_UNKNOWN = 0;
    public final static int EVENT_START_SERVICE = 1, EVENT_ALARM = 2, EVENT_SET_ALARMS = 3, EVENT_UNSET_ALARMS = 4;
    public final static String INTENT_EVENT_KEY = "event";

    private boolean alarmsInitialized = false;

    private ArrayList<PendingIntent> pendingAlarmIntents = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        initAlarms(false);
    }

    private void initAlarms(boolean forceUpdate){
        if(alarmsInitialized == false || forceUpdate == true){

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            ArrayList<AlarmObject> alarmList = Preferences.getAllAlarms(getApplicationContext());
            for (AlarmObject alarm : alarmList) {
                Intent alarmIntent = new Intent(this, AlarmService.class);
                alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                alarmIntent.putExtra(AlarmService.INTENT_EVENT_KEY, AlarmService.EVENT_ALARM);
                alarmIntent.putExtra("message", alarm.getTitle());
                alarmIntent.putExtra("alarm_id", alarm.getID());

                PendingIntent pi = PendingIntent.getService(this, alarm.getID(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pi);

                if(alarm.isEnabled()){
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMS(), pi);
                    pendingAlarmIntents.add(pi);
                }
            }
            alarmsInitialized = true;
        }
    }

    private void unsetAlarms(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        for(PendingIntent pi : pendingAlarmIntents){
            alarmManager.cancel(pi);
        }
        pendingAlarmIntents.clear();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            int eventFlag = intent.getIntExtra(INTENT_EVENT_KEY, EVENT_UNKNOWN);
            switch (eventFlag){
                case EVENT_START_SERVICE:
                    initAlarms(false);
                    break;
                case EVENT_ALARM:
                    onAlarmReceived(intent);
                    break;
                case EVENT_SET_ALARMS:
                    initAlarms(true);
                    break;
                case EVENT_UNSET_ALARMS:
                    unsetAlarms();
                    break;
                case EVENT_UNKNOWN:
                    break;
            }
        }
        return Service.START_STICKY;
    }

    private void onAlarmReceived (Intent intent){
        String msg = intent.getStringExtra("message");
        int id = intent.getIntExtra("alarm_id", -1);
        if(msg == null){
            msg = "";
        }

        AlarmObject alarm = Preferences.getAllAlarms(getApplicationContext()).get(id);
        alarm.setEnabled(false);
        Preferences.addAlarm(getApplicationContext(), alarm);

        Intent editAlarmIntent = new Intent(this, EditAlarmActivity.class);
        editAlarmIntent.putExtra("editObjectID", id);
        editAlarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        TaskStackBuilder.create(this).addNextIntentWithParentStack(editAlarmIntent).startActivities();
    }

    @Override
    public void onDestroy() {
        alarmsInitialized = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
