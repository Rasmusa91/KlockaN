package com.superteam.klockan;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Rasmus on 2016-12-30.
 */

public class Utilities
{
    private static int DEFAULT_MINUTE_PRECISION = 5;

    private static String[] timeStringValues = {
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


    public static String timeToString(int p_Hour, int p_Minute, int p_AM_PM){
        return timeToString(p_Hour, p_Minute, p_AM_PM, DEFAULT_MINUTE_PRECISION);
    }
    public static String timeToString(int p_Hour, int p_Minute, int p_AM_PM, int minutePrecision)
    {
        String res = "";
        String minuteString;
        String pastOrTo;

        int fixedMin = (int) Math.floor(p_Minute / minutePrecision) * minutePrecision;
        int fixedHour = p_Hour == 0 ? 12 : p_Hour;

        //If past 30 minutes of the hour, its gonna be "to <next-hour>" instead of "past <current hour>".
        if(fixedMin > 30)
        {
            fixedHour++;

            if(fixedHour > 12)
            {
                fixedHour = 1;
            }

            fixedMin = 30 + (30 - fixedMin);
            pastOrTo = " to ";
        }else{
            pastOrTo = " past ";
        }
        if(fixedMin == 15 || fixedMin == 45){
            minuteString = "Quarter";
        }else if (fixedMin == 30){
            minuteString = "Half";
        }else{
            minuteString = timeStringValues[fixedMin];
        }

        res = timeStringValues[fixedHour];

        if(fixedMin == 0)
        {
            res += " o'clock";
        }
        else if(fixedMin <= 30)
        {
            res = minuteString + pastOrTo + res;
        }
        else if(fixedMin > 30)
        {
            res = minuteString + pastOrTo + res;
        }

        return res;
    }

    public static String[] getStringHours()
    {
        return new String[] {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve"};
    }

    public static String[] getStringMinutes()
    {
        return timeStringValues;
    }

    public static String[] getStringAMPM()
    {
        return new String[] {"AM", "PM"};
    }
}