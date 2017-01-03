package com.superteam.klockan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by oornmyr on 1/3/17.
 */

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Set up the alarmService
        Intent alarmServiceIntent = new Intent(context, AlarmService.class);
        alarmServiceIntent.putExtra(AlarmService.INTENT_EVENT_KEY, AlarmService.EVENT_START_SERVICE);
        context.startService(alarmServiceIntent);
    }
}
