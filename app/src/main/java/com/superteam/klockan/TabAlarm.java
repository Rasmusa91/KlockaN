package com.superteam.klockan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

/**
 * Created by Rasmus on 2016-12-29.
 */

public class TabAlarm extends TabFragment
{
    private ArrayList<AlarmObject> m_Items;
    private ArrayAdapter<AlarmObject> m_Adapter;
    private View m_View;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m_View = inflater.inflate(R.layout.alarm_layout, container, false);
        return m_View;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize(){
        m_Items = Preferences.getAllAlarms(getContext());

        m_Adapter = new ArrayAdapter<AlarmObject>(m_View.getContext(), R.layout.list_item, R.id.text1, m_Items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ((TextView) view.findViewById(R.id.text1)).setText(m_Items.get(position).toString());
                ((TextView) view.findViewById(R.id.text2)).setText(m_Items.get(position).getTitle());
                view.findViewById(R.id.checked).setVisibility(m_Items.get(position).isEnabled() ? View.VISIBLE : View.GONE);

                return view;
            }
        };

        ListView listView = (ListView) m_View.findViewById(R.id.alarmListView);
        listView.setAdapter(m_Adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditAlarmActivity.class);
                intent.putExtra("editObjectID", m_Items.get(position).getID());
                startActivity(intent);
            }
        });


        Button addAlarmButton = (Button) m_View.findViewById(R.id.addAlarm);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editAlarmIntent = new Intent(getActivity(), EditAlarmActivity.class);
                editAlarmIntent.putExtra("editObjectID", -1);
                startActivity(editAlarmIntent);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onTimeUpdated()
    {

    }

    @Override
    public void onDefaultTimeChanged(long p_TimeDiffMS)
    {
        for(AlarmObject a : m_Items){
            a.setDefaultTimeOffset(a.getDefaultTimeOffset() + p_TimeDiffMS);
        }
        m_Adapter.notifyDataSetChanged();
    }
}