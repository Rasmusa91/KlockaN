package com.superteam.klockan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Rasmus on 2016-12-29.
 */

public class TabTime extends Fragment
{
    private ArrayList<TimeObject> m_Items;
    private ArrayAdapter<TimeObject> m_Adapter;
    private View m_View;

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

        initialize();
    }

    private void initialize()
    {
        m_Items = new ArrayList<TimeObject>();
        m_Adapter = new ArrayAdapter<TimeObject>(m_View.getContext(), android.R.layout.simple_list_item_1, m_Items);

        ImageButton addTimeButton = (ImageButton) m_View.findViewById(R.id.addTime);
        ListView listView = (ListView) m_View.findViewById(R.id.timeView);
        listView.setAdapter(m_Adapter);

        addTimeButton.setTag(this);
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TabTime) v.getTag()).addItem("Test");
            }
        });
    }

    public void addItem(String p_Item)
    {
        m_Items.add(new TimeObject("Test2"));
        m_Adapter.notifyDataSetChanged();
    }
}