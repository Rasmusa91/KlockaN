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
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

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

        final AlarmObject editObject = getEditObject();
        generateTimeVariables(editObject);
        initializePickers();

        if(editObject != null){
            ((TextView)findViewById(R.id.alarmTitle)).setText(editObject.getTitle());
            ((Switch)findViewById(R.id.enabledSwitch)).setChecked(editObject.isEnabled());
        }

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveObject(editObject);
            }
        });
        //TODO Deletebutton
        ((Button) findViewById(R.id.cancelButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateTimeHeader();
    }

    private AlarmObject getEditObject()
    {
        int editID = getIntent().getExtras().getInt("editObjectID");
        return (editID != -1 ? Preferences.getAllAlarms(getApplicationContext()).get(editID) : null);
    }


    private void initializePickers(){
        String[] stringHours = Utilities.getStringHours();
        String[] stringMinutes = Utilities.getStringMinutes();
        String[] stringAMPM = Utilities.getStringAMPM();

        NumberPicker pickerHour = (NumberPicker) findViewById(R.id.numberPickerHour);
        pickerHour.setMinValue(0);
        pickerHour.setMaxValue(stringHours.length - 1);
        pickerHour.setDisplayedValues(stringHours);
        pickerHour.setValue(m_Hour);

        NumberPicker pickerMinute = (NumberPicker) findViewById(R.id.numberPickerMinute);
        pickerMinute.setMinValue(0);
        pickerMinute.setMaxValue(stringMinutes.length - 1);
        pickerMinute.setDisplayedValues(stringMinutes);
        pickerMinute.setValue(m_Minute);


        NumberPicker pickerAMPM = (NumberPicker) findViewById(R.id.numberPickerAMPM);
        pickerAMPM.setMinValue(0);
        pickerAMPM.setMaxValue(stringAMPM.length - 1);
        pickerAMPM.setDisplayedValues(stringAMPM);
        pickerAMPM.setValue((m_AMPM == Calendar.AM) ? 0 : 1);

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
    }

    private void generateTimeVariables(AlarmObject p_EditObject)
    {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        if(p_EditObject != null)
        {
            c.setTimeInMillis(p_EditObject.getTimeInMS());
        }
        m_Hour = c.get(Calendar.HOUR) - 1;
        m_Minute = c.get(Calendar.MINUTE);
        m_AMPM = c.get(Calendar.AM_PM);
    }

    private void saveObject(AlarmObject p_EditObject)
    {
        String title = ((TextView) findViewById(R.id.alarmTitle)).getText().toString();
        boolean isEnabled = ((Switch) findViewById(R.id.enabledSwitch)).isChecked();
        int hour = ((NumberPicker) findViewById(R.id.numberPickerHour)).getValue();
        int minute = ((NumberPicker) findViewById(R.id.numberPickerMinute)).getValue();
        int ampm = ((NumberPicker) findViewById(R.id.numberPickerAMPM)).getValue();
        hour += 1;
        ampm = (ampm == 0 ? Calendar.AM : Calendar.PM);

        Calendar c = Calendar.getInstance();
        long currentTimeInMillis = c.getTimeInMillis();

        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.AM_PM, ampm);

        if(c.getTimeInMillis() <= currentTimeInMillis){
            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        if(title == null){//Make sure there is a title
            title = "";
        }
        int id = (p_EditObject != null ? p_EditObject.getID() : -1);
        Preferences.addAlarm(getApplicationContext(), new AlarmObject(id, title, isEnabled, c.getTimeInMillis()));

        Intent alarmServiceIntent = new Intent(this, AlarmService.class);
        alarmServiceIntent.putExtra(AlarmService.INTENT_EVENT_KEY, AlarmService.EVENT_SET_ALARMS);
        this.startService(alarmServiceIntent);

        Toast.makeText(getApplicationContext(), "Alarm saved", Toast.LENGTH_SHORT);
        finish();
    }

    private void updateTimeHeader()
    {
        int desiredMinutePrecision = 1;
        ((TextView) findViewById(R.id.headerAlarm)).setText(Utilities.timeToString(m_Hour + 1, m_Minute, m_AMPM, desiredMinutePrecision));
    }

}