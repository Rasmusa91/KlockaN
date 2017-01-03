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
    private boolean m_Default;
    private long m_OffsetMS;

    public TimeObject(int p_ID, String p_Title, boolean p_Default, long p_OffsetMS)
    {
        m_ID = p_ID;
        m_Title = p_Title;
        m_Default = p_Default;
        m_OffsetMS = p_OffsetMS;
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

    public void setDefault(boolean p_Default)
    {
        m_Default = p_Default;
    }

    public boolean getDefault()
    {
        return m_Default;
    }

    public long getOffsetMS()
    {
        return m_OffsetMS;
    }

    @Override
    public String toString()
    {
        return getTimeInText();
    }

    public long getTimeInMillis()
    {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis() + m_OffsetMS;
    }

    private String getTimeInText()
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getTimeInMillis());
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int amPM = c.get(Calendar.AM_PM);

        return Utilities.timeToString(hour, minute, amPM, 1);
    }
}

