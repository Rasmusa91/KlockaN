package com.superteam.klockan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.TaskStackBuilder;

import java.util.ArrayList;

/**
 * Created by oornmyr on 1/3/17.
 */

public class BackgroundService extends Service {

    private final static int EVENT_UNKNOWN = 0;
    public final static int EVENT_START_SERVICE = 1, EVENT_ALARM = 2, EVENT_SET_ALARMS = 3, EVENT_UNSET_ALARMS = 4;
    public final static String INTENT_EVENT_KEY = "event";

    private boolean alarmsInitialized = false;

    private ArrayList<PendingIntent> pendingAlarmIntents = new ArrayList<>();

    private ShakeHandler m_ShakeHandler;
    private TextToSpeechHandler m_TextToSpeechHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        initAlarms(false);

        m_TextToSpeechHandler = new TextToSpeechHandler(getApplicationContext());
        m_ShakeHandler = new ShakeHandler(this, new Callback() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCallback(Object o) {
                readTime();
            }
        });
    }

    private void initAlarms(boolean forceUpdate){
        if(alarmsInitialized == false || forceUpdate == true){

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            ArrayList<AlarmObject> alarmList = Preferences.getAllAlarms(getApplicationContext());
            for (AlarmObject alarm : alarmList) {
                Intent alarmIntent = new Intent(this, BackgroundService.class);
                alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                alarmIntent.putExtra(BackgroundService.INTENT_EVENT_KEY, BackgroundService.EVENT_ALARM);
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

        Intent startAlarmIntent = new Intent(this, AlarmActivity.class);
        startAlarmIntent.putExtra("alarmID", id);
        startAlarmIntent.putExtra("alarmTitle", msg);
        startAlarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        TaskStackBuilder.create(this).addNextIntentWithParentStack(startAlarmIntent).startActivities();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void readTime()
    {
        String time = Utilities.getDefaultTimeObject(getApplicationContext()).toString();
        m_TextToSpeechHandler.speak("The time is " + time);
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
}
