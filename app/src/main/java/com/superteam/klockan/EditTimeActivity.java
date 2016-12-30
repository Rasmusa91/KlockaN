package com.superteam.klockan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

public class EditTimeActivity extends AppCompatActivity
{
    private int m_Hour;
    private int m_Minute;
    private int m_AMPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time);

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

        updateTimeHeader();
    }

    private void updateTimeHeader()
    {
        ((TextView) findViewById(R.id.headerTime)).setText(Utilities.timeToString(m_Hour + 1, m_Minute, m_AMPM));
    }
}
