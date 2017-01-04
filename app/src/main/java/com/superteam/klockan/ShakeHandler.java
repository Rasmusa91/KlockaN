package com.superteam.klockan;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Rasmus on 2017-01-01.
 */

public class ShakeHandler implements SensorEventListener
{
    private static final int SHAKE_THRESHOLD = 2000;

    private Callback m_Callback;

    private SensorManager m_SensorManager;
    private long m_LastUpdate;
    private float m_LastX;
    private float m_LastY;
    private float m_LastZ;

    public ShakeHandler(Context p_Context, Callback p_Callback)
    {
        m_Callback = p_Callback;

        m_SensorManager = (SensorManager) p_Context.getSystemService(p_Context.SENSOR_SERVICE);
        m_SensorManager.registerListener(this, m_SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float[] values = event.values;
        long currTime = System.currentTimeMillis();

        if ((currTime - m_LastUpdate) > 100)
        {
            long diff = (currTime - m_LastUpdate);
            m_LastUpdate = currTime;

            float x = values[0];
            float y = values[1];
            float z = values[2];

            float speed = Math.abs(x + y + z - m_LastX - m_LastY - m_LastZ) / diff * 10000;

            if (speed > SHAKE_THRESHOLD) {
                m_Callback.onCallback(null);
            }

            m_LastX = x;
            m_LastY = y;
            m_LastZ = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
