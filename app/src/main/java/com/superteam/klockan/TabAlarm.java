package com.superteam.klockan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by Rasmus on 2016-12-29.
 */

public class TabAlarm extends TabFragment
{
    private ArrayList<TimeObject> m_Items;
    private ArrayAdapter<TimeObject> m_Adapter;
    private View m_View;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m_View = inflater.inflate(R.layout.alarm_layout, container, false);

        return m_View;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageButton addAlarmButton = (ImageButton) m_View.findViewById(R.id.addAlarm);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent editTimeActivity = new Intent(getActivity(), EditTimeActivity.class);
                //startActivity(editTimeActivity);
                Intent editAlarmIntent = new Intent(getActivity(), EditAlarmActivity.class);
                startActivity(editAlarmIntent);
            }
        });

    }

    @Override
    public void onTimeUpdated()
    {

    }
}