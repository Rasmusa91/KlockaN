package com.superteam.klockan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Rasmus on 2016-12-29.
 */

public class TabTime extends TabFragment
{
    private ArrayList<TimeObject> m_Items;
    private ArrayAdapter<TimeObject> m_Adapter;
    private View m_View;
    private int m_DefaultTimeObjectIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m_View = inflater.inflate(R.layout.time_layout, container, false);

        return m_View;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();

        initialize();
    }

    @Override
    public void onTimeUpdated()
    {
        if(m_View == null)
        {
            return;
        }

        m_Adapter.notifyDataSetChanged();
        updateHeaderTime();
    }

    private void initialize()
    {
        m_Items = Preferences.getAllTimes(getContext());

        if(m_Items.size() == 0)
        {
            addDefaultTimeObject();
            m_Items = Preferences.getAllTimes(getContext());
        }

        m_Adapter = new ArrayAdapter<TimeObject>(m_View.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, m_Items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(m_Items.get(position).toString());
                text2.setText(m_Items.get(position).getTitle());

                return view;
            }
        };

        Button addTimeButton = (Button) m_View.findViewById(R.id.addTime);
        ListView listView = (ListView) m_View.findViewById(R.id.timeView);
        listView.setAdapter(m_Adapter);

        addTimeButton.setTag(this);
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editTimeActivity = new Intent(getActivity(), EditTimeActivity.class);
                startActivity(editTimeActivity);
            }
        });

        findDefaultTimeIndex();
        updateHeaderTime();
    }

    private void findDefaultTimeIndex()
    {
        m_DefaultTimeObjectIndex = -1;

        for(int i = 0; i < m_Items.size() && m_DefaultTimeObjectIndex == -1; i++)
        {
            if(m_Items.get(i).getDefault())
            {
                m_DefaultTimeObjectIndex = i;
            }
        }

        if(m_DefaultTimeObjectIndex == -1)
        {
            m_DefaultTimeObjectIndex = 0;
        }
    }

    private void updateHeaderTime()
    {
        if(m_Items.size() == 0)
        {
            return;

        }
        ((TextView) m_View.findViewById(R.id.headerTime)).setText(m_Items.get(m_DefaultTimeObjectIndex).toString());
    }

    private void addDefaultTimeObject()
    {
        Preferences.AddTime(getContext(), new TimeObject(0, "Default", true, 0));
    }
}