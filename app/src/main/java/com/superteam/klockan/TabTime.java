package com.superteam.klockan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

    @Override
    public void onTimeUpdated()
    {
        if(m_View == null)
        {
            return;
        }

        m_Adapter.notifyDataSetChanged();
    }

    private void initialize()
    {
        m_Items = new ArrayList<TimeObject>();
        m_Adapter = new ArrayAdapter<TimeObject>(m_View.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, m_Items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(m_Items.get(position).toString());
                //text2.setText(m_Items.get(position).getAge());
                text2.setText("Test");

                return view;
            }
        };

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