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
    private int m_ID;
    private String m_Title;

    public TimeObject(int p_ID, String p_Title)
    {
        m_ID = p_ID;
        m_Title = p_Title;
    }

    public void setID(int p_ID)
    {
        m_ID = p_ID;
    }

    public int getID()
    {
        return m_ID;
    }

    public String getTitle()
    {
        return m_Title;
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

