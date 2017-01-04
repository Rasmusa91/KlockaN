package com.superteam.klockan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private boolean m_IsPaused;

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
        m_IsPaused = false;
    }

    @Override
    public void onPause() {
        super.onPause();

        m_IsPaused = true;
    }

    @Override
    public void onTimeUpdated()
    {
        if(m_View == null || m_Adapter == null || m_IsPaused)
        {
            return;
        }

        m_Adapter.notifyDataSetChanged();
        updateHeaderTime();
    }

    @Override
    public void onDefaultTimeChanged(long p_TimeDiffMS)
    {

    }

    private void initialize()
    {
        m_Items = Preferences.getAllTimes(getContext());

        m_Adapter = new ArrayAdapter<TimeObject>(m_View.getContext(), R.layout.list_item, R.id.text1, m_Items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ((TextView) view.findViewById(R.id.text1)).setText(m_Items.get(position).toString());
                ((TextView) view.findViewById(R.id.text2)).setText(m_Items.get(position).getTitle());
                view.findViewById(R.id.checked).setVisibility(m_Items.get(position).getDefault() ? View.VISIBLE : View.GONE);

                return view;
            }
        };

        Button addTimeButton = (Button) m_View.findViewById(R.id.addTime);
        ListView listView = (ListView) m_View.findViewById(R.id.timeView);
        listView.setAdapter(m_Adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startEditTimeActivity(m_Items.get(position).getID());
            }
        });

        addTimeButton.setTag(this);
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTimeActivity(-1);
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

    private void startEditTimeActivity(int p_ID)
    {
        Intent intent = new Intent(getActivity(), EditTimeActivity.class);
        intent.putExtra("editObjectID", p_ID);
        startActivity(intent);
    }

    public String getCurrentTimeString()
    {
        String res = "";

        if(m_Items != null && m_Items.size() > m_DefaultTimeObjectIndex) {
            res = m_Items.get(m_DefaultTimeObjectIndex).toString();
        }

        return res;
    }
}