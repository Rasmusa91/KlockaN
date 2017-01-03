package com.superteam.klockan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by oornmyr on 12/31/16.
 */

public class EditAlarmActivity extends AppCompatActivity {
    private int m_Hour;
    private int m_Minute;
    private int m_AMPM;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        initialize();
    }

    private void initialize(){
        this.context = this;

        String[] stringHours = Utilities.getStringHours();
        String[] stringMinutes = Utilities.getStringMinutes();
        String[] stringAMPM = Utilities.getStringAMPM();

        NumberPicker pickerHour = (NumberPicker) findViewById(R.id.numberPickerHour);
        pickerHour.setMinValue(0);
        pickerHour.setMaxValue(stringHours.length - 1);
        pickerHour.setDisplayedValues(stringHours);

        NumberPicker pickerMinute = (NumberPicker) findViewById(R.id.numberPickerMinute);
        pickerMinute.setMinValue(0);
        pickerMinute.setMaxValue(stringMinutes.length - 1);
        pickerMinute.setDisplayedValues(stringMinutes);

        NumberPicker pickerAMPM = (NumberPicker) findViewById(R.id.numberPickerAMPM);
        pickerAMPM.setMinValue(0);
        pickerAMPM.setMaxValue(stringAMPM.length - 1);
        pickerAMPM.setDisplayedValues(stringAMPM);

        pickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                m_Hour = newVal;
                updateTimeHeader();
            }
        });

        pickerMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                m_Minute = newVal;
                updateTimeHeader();
            }
        });

        pickerAMPM.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                m_AMPM = newVal;
                updateTimeHeader();
            }
        });

        ((Button) findViewById(R.id.cancelButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((Button) findViewById(R.id.saveButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String title = ((TextView) findViewById(R.id.timeTitle)).getText().toString();
                //Preferences.AddTime(getApplicationContext(), new TimeObject(-1, title));

                String title = ((TextView) findViewById(R.id.alarmTitle)).getText().toString();
                boolean isDefault = ((Switch) findViewById(R.id.enabledSwitch)).isChecked();
                int hour = ((NumberPicker) findViewById(R.id.numberPickerHour)).getValue();
                int minute = ((NumberPicker) findViewById(R.id.numberPickerMinute)).getValue();
                int ampm = ((NumberPicker) findViewById(R.id.numberPickerAMPM)).getValue();
                hour += 1;
                ampm = (ampm == 0 ? Calendar.AM : Calendar.PM);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR, hour);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.AM_PM, ampm);

                //TODO Save this intent in order to cancel it later.
                final Intent alarmIntent = new Intent(context, AlarmService.class);
                alarmIntent.putExtra("message", title);

                alarmIntent.putExtra(AlarmService.INTENT_EVENT_KEY, AlarmService.EVENT_ALARM);
                PendingIntent pi = PendingIntent.getService(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

                /*
                myIntent.putExtra("extra", "yes");
                myIntent.putExtra("quote id", String.valueOf(richard_quote));
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

                setAlarmText("Alarm set to " + hour_string + ":" + minute_string);
                */

                finish();
            }
        });

        updateTimeHeader();
    }

    private void updateTimeHeader()
    {
        int desiredMinutePrecision = 1;
        ((TextView) findViewById(R.id.headerAlarm)).setText(Utilities.timeToString(m_Hour + 1, m_Minute, m_AMPM, desiredMinutePrecision));
    }

}
