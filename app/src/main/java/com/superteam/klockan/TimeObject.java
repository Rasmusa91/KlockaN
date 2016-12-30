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
    HashMap<Integer, String> m_Translation;

    public TimeObject(String p_Text)
    {
        m_Text = p_Text;

        buildTranslationMap();
    }

    private void buildTranslationMap()
    {
        m_Translation = new HashMap<Integer, String>();
        m_Translation.put(0, "twelve");
        m_Translation.put(1, "one");
        m_Translation.put(2, "two");
        m_Translation.put(3, "three");
        m_Translation.put(4, "four");
        m_Translation.put(5, "five");
        m_Translation.put(6, "six");
        m_Translation.put(7, "seven");
        m_Translation.put(8, "eight");
        m_Translation.put(9, "nine");
        m_Translation.put(10, "ten");
        m_Translation.put(11, "eleven");
        m_Translation.put(12, "twelve");
        m_Translation.put(15, "quarter");
        m_Translation.put(20, "twenty");
        m_Translation.put(25, "twenty-five");
        m_Translation.put(30, "half");
        m_Translation.put(35, "twenty-five");
        m_Translation.put(40, "twenty");
        m_Translation.put(45, "quarter");
        m_Translation.put(50, "ten");
        m_Translation.put(55, "five");
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

        int fixedMin = (int) Math.floor(minute / 5) * 5;
        int fixedHour = hour;

        if(fixedMin > 30)
        {
            fixedHour++;

            if(fixedHour > 12)
            {
                fixedHour = 1;
            }
        }

        res = m_Translation.get(fixedHour);

        if(fixedMin == 0)
        {
            res += " o'clock";
        }
        else if(fixedMin <= 30)
        {
            res = m_Translation.get(fixedMin) + " past " + res;
        }
        else if(fixedMin > 30)
        {
            res = m_Translation.get(fixedMin) + " to " + res;
        }

        //res += " " + (amPM == Calendar.PM ? "PM" : "AM");

        return res;
    }
}

