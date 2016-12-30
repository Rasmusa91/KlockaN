package com.superteam.klockan;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Rasmus on 2016-12-30.
 */

public class Utilities
{
    public static String timeToString(int p_Hour, int p_Minute, int p_AM_PM)
    {
        HashMap<Integer, String> m_Translation = new HashMap<Integer, String>();
        m_Translation.put(0, "Twelve");
        m_Translation.put(1, "One");
        m_Translation.put(2, "Two");
        m_Translation.put(3, "Three");
        m_Translation.put(4, "Four");
        m_Translation.put(5, "Five");
        m_Translation.put(6, "Six");
        m_Translation.put(7, "Seven");
        m_Translation.put(8, "Eight");
        m_Translation.put(9, "Nine");
        m_Translation.put(10, "Ten");
        m_Translation.put(11, "Eleven");
        m_Translation.put(12, "Twelve");
        m_Translation.put(15, "Quarter");
        m_Translation.put(20, "Twenty");
        m_Translation.put(25, "Twenty-five");
        m_Translation.put(30, "Half");
        m_Translation.put(35, "Twenty-five");
        m_Translation.put(40, "Twenty");
        m_Translation.put(45, "Quarter");
        m_Translation.put(50, "Ten");
        m_Translation.put(55, "Five");

        String res = "";

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, p_Hour);
        c.set(Calendar.MINUTE, p_Minute);
        c.set(Calendar.AM_PM, p_AM_PM);

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

    public static String[] getStringHours()
    {
        return new String[] {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve"};
    }

    public static String[] getStringMinutes()
    {
        return new String[]{
            "Zero",

            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine",

            "Ten",
            "Eleven",
            "Twelve",
            "Thirteen",
            "Fourteen",
            "Fifteen",
            "Sixteen",
            "Seventeen",
            "Eighteen",
            "Nineteen",

            "Twenty",
            "Twenty-one",
            "Twenty-two",
            "Twenty-three",
            "Twenty-four",
            "Twenty-five",
            "Twenty-six",
            "Twenty-seven",
            "Twenty-eight",
            "Twenty-nine",

            "Thirty",
            "Thirty-one",
            "Thirty-two",
            "Thirty-three",
            "Thirty-four",
            "Thirty-five",
            "Thirty-six",
            "Thirty-seven",
            "Thirty-eight",
            "Thirty-nine",

            "Forty",
            "Forty-one",
            "Forty-two",
            "Forty-three",
            "Forty-four",
            "Forty-five",
            "Forty-six",
            "Forty-seven",
            "Forty-eight",
            "Forty-nine",

            "Fifty",
            "Fifty-one",
            "Fifty-two",
            "Fifty-three",
            "Fifty-four",
            "Fifty-five",
            "Fifty-six",
            "Fifty-seven",
            "Fifty-eight",
            "Fifty-nine"
        };
    }

    public static String[] getStringAMPM()
    {
        return new String[] {"AM", "PM"};
    }
}
