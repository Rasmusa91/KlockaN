package com.superteam.klockan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rasmus on 2016-12-30.
 */

public class Preferences
{
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

    public static void ClearPreferences(Context p_Context)
    {
        SharedPreferences.Editor editor = getPrefEditor(p_Context);
        editor.clear();
        editor.apply();

    }

    public static void AddTime(Context p_Context, TimeObject p_TimeObject)
    {
        ArrayList<TimeObject> objects = getAllTimes(p_Context);
        objects.add(p_TimeObject);
        normalizeTimeObjects(objects, p_TimeObject.getDefault());

        SharedPreferences.Editor editor = getPrefEditor(p_Context);
        editor.putString("clocks", getJsonFromTimeObjects(objects));
        editor.apply();
    }

    public static ArrayList<TimeObject> getAllTimes(Context p_Context)
    {
        SharedPreferences settings = getPrefSettings(p_Context);
        String timeObjectsJson = settings.getString("clocks", null);

        return getTimeObjectsFromJson(timeObjectsJson);
    }

    private static void normalizeTimeObjects(ArrayList<TimeObject> p_TimeObjects, boolean p_NewDefault)
    {
        for(int i = 0; i < p_TimeObjects.size(); i++)
        {
            p_TimeObjects.get(i).setID(i);

            if(p_NewDefault && i < p_TimeObjects.size() - 1)
            {
                p_TimeObjects.get(i).setDefault(false);
            }
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
}
