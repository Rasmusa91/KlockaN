package com.superteam.klockan;

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
        return m_Text;
    }
}

