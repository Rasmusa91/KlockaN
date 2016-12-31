package com.superteam.klockan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by oornmyr on 12/31/16.
 */

public class EditAlarmActivity extends AppCompatActivity {
    private int m_Hour;
    private int m_Minute;
    private int m_AMPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        initialize();
    }

    private void initialize(){
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
