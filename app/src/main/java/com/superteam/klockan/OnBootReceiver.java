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
        Intent alarmServiceIntent = new Intent(context, BackgroundService.class);
        alarmServiceIntent.putExtra(BackgroundService.INTENT_EVENT_KEY, BackgroundService.EVENT_START_SERVICE);
        alarmServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(alarmServiceIntent);
    }
}
