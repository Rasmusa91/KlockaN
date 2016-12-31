package com.superteam.klockan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

public class EditTimeActivity extends AppCompatActivity
{
    private int m_Hour;
    private int m_Minute;
    private int m_AMPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time);

        initialize();
    }

    private void initialize()
    {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        m_Hour = c.get(Calendar.HOUR) - 1;
        m_Minute = c.get(Calendar.MINUTE);
        m_AMPM = c.get(Calendar.AM_PM);

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

        ((Button) findViewById(R.id.cancelButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((Button) findViewById(R.id.saveButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((TextView) findViewById(R.id.timeTitle)).getText().toString();
                boolean isDefault = ((Switch) findViewById(R.id.useAsDefault)).isChecked();
                int hour = ((NumberPicker) findViewById(R.id.numberPickerHour)).getValue();
                int minute = ((NumberPicker) findViewById(R.id.numberPickerMinute)).getValue();
                int ampm = ((NumberPicker) findViewById(R.id.numberPickerAMPM)).getValue();
                hour += 1;
                ampm = (ampm == 0 ? Calendar.AM : Calendar.PM);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR, hour);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.AM_PM, ampm);

                Calendar curr = Calendar.getInstance();

                long offset = c.getTimeInMillis() - curr.getTimeInMillis();

                if(validateValues(title))
                {
                    Preferences.AddTime(getApplicationContext(), new TimeObject(-1, title, isDefault, offset));

                    finish();
                }
                else
                {
                    toastError();
                }
            }
        });

        ((TableRow) findViewById(R.id.editRow)).setVisibility(View.GONE);

        updateTimeHeader();
    }

    private void updateTimeHeader()
    {
        ((TextView) findViewById(R.id.headerTime)).setText(Utilities.timeToString(m_Hour + 1, m_Minute, m_AMPM));
    }

    private boolean validateValues(String p_Title)
    {
        return p_Title != null && !p_Title.isEmpty();
    }

    private void toastError()
    {
        Toast.makeText(this, "Please enter a title", Toast.LENGTH_LONG).show();
    }
}
