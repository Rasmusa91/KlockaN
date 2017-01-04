package com.superteam.klockan;

import java.util.Calendar;

/**
 * Created by oornmyr on 2017-01-03.
 */

public class AlarmObject
{
    private int m_ID;
    private String m_Title;
    private boolean m_Enabled;
    private long m_TimeInMillis;

    private long m_DefaultTimeOffset = 0;

    public AlarmObject(int p_ID, String p_Title, boolean p_Enabled, long p_TimeInMillis, long p_DefaultTimeOffset)
    {
        m_ID = p_ID;
        m_Title = p_Title;
        m_Enabled = p_Enabled;
        m_TimeInMillis = p_TimeInMillis;
        m_DefaultTimeOffset = p_DefaultTimeOffset;
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

    public void setEnabled(boolean p_Enabled)
    {
        m_Enabled = p_Enabled;
    }

    public boolean isEnabled()
    {
        return m_Enabled;
    }

    public long getTimeInMS()
    {
        return m_TimeInMillis;
    }

    public void setDefaultTimeOffset(long offset){
        m_DefaultTimeOffset = offset;
    }
    public long getDefaultTimeOffset(){return m_DefaultTimeOffset;}

    @Override
    public String toString()
    {
        return getTimeInText();
    }

    private String getTimeInText()
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getTimeInMS() + m_DefaultTimeOffset);
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int amPM = c.get(Calendar.AM_PM);

        return Utilities.timeToString(hour, minute, amPM, 1);
    }
}

