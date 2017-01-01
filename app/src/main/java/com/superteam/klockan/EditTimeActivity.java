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
        TimeObject editObject = getEditObject();
        generateTimeVariables(editObject);
        initializePickers();

        if(editObject == null) {
            initializeForAdd();
        }
        else {
            initializeForEdit(editObject);
        }

        updateTimeHeader();
    }

    private TimeObject getEditObject()
    {
        int editID = getIntent().getExtras().getInt("editObjectID");
        return (editID != -1 ? Preferences.getAllTimes(getApplicationContext()).get(editID) : null);
    }

    private void generateTimeVariables(TimeObject p_EditObject)
    {
        Calendar c = Calendar.getInstance(Locale.getDefault());

        if(p_EditObject != null)
        {
            c.setTimeInMillis(c.getTimeInMillis() + p_EditObject.getOffsetMS());
        }

        m_Hour = c.get(Calendar.HOUR) - 1;
        m_Minute = c.get(Calendar.MINUTE);
        m_AMPM = c.get(Calendar.AM_PM);
    }

    private void initializePickers()
    {
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

    private void initializeForAdd()
    {
        findViewById(R.id.editRow).setVisibility(View.GONE);

        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveObject();
            }
        });
    }

    private void initializeForEdit(final TimeObject p_EditObject)
    {
        findViewById(R.id.addRow).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.timeTitle)).setText(p_EditObject.getTitle());
        ((Switch) findViewById(R.id.useAsDefault)).setChecked(p_EditObject.getDefault());

        findViewById(R.id.cancelButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.saveButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveObject(p_EditObject);
            }
        });

        findViewById(R.id.deleteButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteObject(p_EditObject);
            }
        });
    }

    private void updateTimeHeader()
    {
        ((TextView) findViewById(R.id.headerTime)).setText(Utilities.timeToString(m_Hour + 1, m_Minute, m_AMPM, 1));
    }

    private void saveObject()
    {
        saveObject(null);
    }

    private void saveObject(TimeObject p_EditObject)
    {
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
            int id = (p_EditObject != null ? p_EditObject.getID() : -1);
            Preferences.addTime(getApplicationContext(), new TimeObject(id, title, isDefault, offset));

            finish();
        }
        else
        {
            toastError();
        }
    }

    private void deleteObject(TimeObject p_EditObject)
    {
        Preferences.deleteTime(getApplicationContext(), p_EditObject);
        finish();
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
