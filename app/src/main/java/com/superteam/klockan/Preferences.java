package com.superteam.klockan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rasmus on 2016-12-30.
 */

public class Preferences
{
    private static ArrayList<TimeObject> m_TimeObjects;
    private static Callback m_DefaultTimeChangedCallback;
    private static ArrayList<AlarmObject> m_AlarmObjects;

    private static SharedPreferences getPrefSettings(Context p_Context)
    {
        SharedPreferences settings = p_Context.getSharedPreferences("Preferences", 0);

        return settings;
    }

    private static SharedPreferences.Editor getPrefEditor(Context p_Context)
    {
        SharedPreferences.Editor editor = getPrefSettings(p_Context).edit();

        return editor;
    }

    public static void setDefaultTimeChangedCallback(Callback p_Callback)
    {
        m_DefaultTimeChangedCallback = p_Callback;
    }

    public static void clearPreferences(Context p_Context)
    {
        SharedPreferences.Editor editor = getPrefEditor(p_Context);
        editor.clear();
        editor.apply();
    }

    public static void addAlarm(Context p_Context, AlarmObject p_AlarmObject){
        ArrayList<AlarmObject> objects = getAllAlarms(p_Context);

        if(p_AlarmObject.getID() == -1 || objects.size() <= p_AlarmObject.getID()) {
            objects.add(p_AlarmObject);
        }
        else {
            objects.set(p_AlarmObject.getID(), p_AlarmObject);
        }

        normalizeAlarmObjects(objects, p_AlarmObject);
        saveAlarms(p_Context, objects);

        Intent alarmServiceIntent = new Intent(p_Context, BackgroundService.class);
        alarmServiceIntent.putExtra(BackgroundService.INTENT_EVENT_KEY, BackgroundService.EVENT_SET_ALARMS);
        p_Context.startService(alarmServiceIntent);
    }
    private static void saveAlarms(Context p_Context, ArrayList<AlarmObject> p_AlarmObjects)
    {
        m_AlarmObjects = p_AlarmObjects;

        SharedPreferences.Editor editor = getPrefEditor(p_Context);
        editor.putString("alarms", getJsonFromAlarmObjects(p_AlarmObjects));
        editor.apply();
    }
    private static String getJsonFromAlarmObjects(ArrayList<AlarmObject> p_Objects)
    {
        String res = "[";

        for(int i = 0; i < p_Objects.size(); i++)
        {
            res += "{" +
                    "\"id\" : " + p_Objects.get(i).getID() + ", " +
                    "\"title\" : \"" + p_Objects.get(i).getTitle() + "\", " +
                    "\"enabled\" : " + p_Objects.get(i).isEnabled() + ", " +
                    "\"timeinmillis\" : " + p_Objects.get(i).getTimeInMS() + "}";

            if(i < p_Objects.size() - 1) {
                res += ",";
            }
        }

        res += "]";

        return res;
    }
    private static void normalizeAlarmObjects(ArrayList<AlarmObject> p_AlarmObjects, AlarmObject p_NewAlarm)
    {
        for(int i = 0; i < p_AlarmObjects.size(); i++)
        {
            p_AlarmObjects.get(i).setID(i);
        }
    }

    public static ArrayList<AlarmObject> getAllAlarms(Context p_Context){
        if(m_AlarmObjects == null)
        {
            SharedPreferences settings = getPrefSettings(p_Context);
            String alarmObjectsJson = settings.getString("alarms", null);

            m_AlarmObjects = getAlarmObjectsFromJson(p_Context, alarmObjectsJson);
        }

        return m_AlarmObjects;
    }
    private static ArrayList<AlarmObject> getAlarmObjectsFromJson(Context p_Context, String p_ObjectsJson)
    {
        ArrayList<AlarmObject> alarmObjects = new ArrayList<AlarmObject>();

        if(p_ObjectsJson != null)
        {
            try {
                JSONArray reader = new JSONArray(p_ObjectsJson);

                for(int i = 0; i < reader.length(); i++)
                {
                    JSONObject alarm = reader.getJSONObject(i);
                    alarmObjects.add(new AlarmObject(alarm.getInt("id"), alarm.getString("title"), alarm.getBoolean("enabled"), alarm.getLong("timeinmillis"), Utilities.getCurrentTimeDiffMS(p_Context)));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return alarmObjects;
    }
    public static void deleteAlarm(Context p_Context, AlarmObject p_AlarmObject)
    {
        Intent unsetAlarmIntent = new Intent(p_Context, BackgroundService.class);
        unsetAlarmIntent.putExtra(BackgroundService.INTENT_EVENT_KEY, BackgroundService.EVENT_UNSET_ALARMS);
        p_Context.startService(unsetAlarmIntent);

        ArrayList<AlarmObject> objects = getAllAlarms(p_Context);
        objects.remove(p_AlarmObject);

        normalizeAlarmObjects(objects, p_AlarmObject);
        saveAlarms(p_Context, objects);

        Intent setAlarmIntent = new Intent(p_Context, BackgroundService.class);
        setAlarmIntent.putExtra(BackgroundService.INTENT_EVENT_KEY, BackgroundService.EVENT_SET_ALARMS);
        p_Context.startService(setAlarmIntent);
    }


    public static void addTime(Context p_Context, TimeObject p_TimeObject)
    {
        long currentDefaultOffset = Utilities.getCurrentTimeDiffMS(p_Context);
        ArrayList<TimeObject> objects = getAllTimes(p_Context);

        if(p_TimeObject.getID() == -1 || objects.size() <= p_TimeObject.getID()) {
            objects.add(p_TimeObject);
        }
        else {
            objects.set(p_TimeObject.getID(), p_TimeObject);
        }

        normalizeTimeObjects(objects, p_TimeObject);
        saveObjects(p_Context, objects);

        checkDefaultTimeDiff(p_Context, currentDefaultOffset);
    }

    public static void deleteTime(Context p_Context, TimeObject p_TimeObject)
    {
        long currentDefaultOffset = Utilities.getCurrentTimeDiffMS(p_Context);
        ArrayList<TimeObject> objects = getAllTimes(p_Context);
        objects.remove(p_TimeObject);

        normalizeTimeObjects(objects, p_TimeObject);
        saveObjects(p_Context, objects);

        checkDefaultTimeDiff(p_Context, currentDefaultOffset);
    }

    public static ArrayList<TimeObject> getAllTimes(Context p_Context)
    {
        if(m_TimeObjects == null)
        {
            SharedPreferences settings = getPrefSettings(p_Context);
            String timeObjectsJson = settings.getString("clocks", null);

            m_TimeObjects = getTimeObjectsFromJson(timeObjectsJson);
        }

        if(m_TimeObjects.size() == 0) {
            addDefaultTimeObject();
        }

        return m_TimeObjects;
    }

    private static void saveObjects(Context p_Context, ArrayList<TimeObject> p_TimeObjects)
    {
        m_TimeObjects = p_TimeObjects;

        SharedPreferences.Editor editor = getPrefEditor(p_Context);
        editor.putString("clocks", getJsonFromTimeObjects(p_TimeObjects));
        editor.apply();
    }

    private static void normalizeTimeObjects(ArrayList<TimeObject> p_TimeObjects, TimeObject p_NewObject)
    {
        boolean defaultFound = false;

        for(int i = 0; i < p_TimeObjects.size(); i++)
        {
            p_TimeObjects.get(i).setID(i);

            if(p_NewObject != null && p_NewObject.getDefault())
            {
                p_TimeObjects.get(i).setDefault(p_TimeObjects.get(i) == p_NewObject ? true : false);
            }

            if(p_TimeObjects.get(i).getDefault()) {
                defaultFound = true;
            }
        }

        if(!defaultFound && p_TimeObjects.size() > 0) {
            p_TimeObjects.get(0).setDefault(true);
        }
    }

    private static String getJsonFromTimeObjects(ArrayList<TimeObject> p_Objects)
    {
        String res = "[";

        for(int i = 0; i < p_Objects.size(); i++)
        {
            res += "{" +
                    "\"id\" : " + p_Objects.get(i).getID() + ", " +
                    "\"title\" : \"" + p_Objects.get(i).getTitle() + "\", " +
                    "\"default\" : " + p_Objects.get(i).getDefault() + ", " +
                    "\"offset\" : " + p_Objects.get(i).getOffsetMS() + "}";

            if(i < p_Objects.size() - 1) {
                res += ",";
            }
        }

        res += "]";

        return res;
    }

    private static ArrayList<TimeObject> getTimeObjectsFromJson(String p_ObjectsJson)
    {
        ArrayList<TimeObject> timeObjects = new ArrayList<TimeObject>();

        if(p_ObjectsJson != null)
        {
            try {
                JSONArray reader = new JSONArray(p_ObjectsJson);

                for(int i = 0; i < reader.length(); i++)
                {
                    JSONObject clock = reader.getJSONObject(i);
                    timeObjects.add(new TimeObject(clock.getInt("id"), clock.getString("title"), clock.getBoolean("default"), clock.getLong("offset")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return timeObjects;
    }

    private static void addDefaultTimeObject()
    {
        m_TimeObjects.add(new TimeObject(0, "Default", true, 0));
    }

    private static void checkDefaultTimeDiff(Context p_Context, long p_LastTimeDiffMS)
    {
        long currentLastTimeMS = Utilities.getCurrentTimeDiffMS(p_Context);

        if(currentLastTimeMS != p_LastTimeDiffMS)
        {
            if(m_DefaultTimeChangedCallback != null)
            {
                m_DefaultTimeChangedCallback.onCallback(currentLastTimeMS - p_LastTimeDiffMS);
            }
        }
    }
}
