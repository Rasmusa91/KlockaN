package com.superteam.klockan;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Rasmus on 2016-12-29.
 */

public class TimeObject
{
    private String m_Text;

    public TimeObject(String p_Text)
    {
        m_Text = p_Text;
    }

    @Override
    public String toString()
    {
        return getTimeInText();
    }

    private String getTimeInText()
    {
        String res = "";

        Calendar c = Calendar.getInstance(Locale.getDefault());
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int amPM = c.get(Calendar.AM_PM);

        return Utilities.timeToString(hour, minute, amPM);
    }
}

