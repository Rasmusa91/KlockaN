package com.superteam.klockan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by William on 2017-01-4.
 */

public class TabStopwatch extends TabFragment
{
    private ArrayList<AlarmObject> m_Items;
    private ArrayAdapter<AlarmObject> m_Adapter;
    private View m_View;

    Button btnStart, btnPause, btnLap;
    TextView txtTimer;
    Handler customHandler = new Handler();
    LinearLayout container;

    long    startTime= 0L,
            timeInMilliseconds=0L,
            timeSwapBuff=0L,
            updateTime=0L;

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run(){
            timeInMilliseconds = SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwapBuff+timeInMilliseconds;
            int milliseconds = (int) (updateTime%100);
            int secs=(int)(updateTime/1000);
            int mins=secs/60;
            int hours=mins/24;
            secs%=60;

            txtTimer.setText(String.format("%02d",hours)+":"
                            +String.format("%02d",mins)+":"
                            +String.format("%02d",secs)+":"
                            +String.format("%02d",milliseconds));
            customHandler.postDelayed(this,0);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m_View = inflater.inflate(R.layout.stopwatch_layout, container, false);
        return m_View;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize() {
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
        txtTimer = (TextView)m_View.findViewById(R.id.txtTimeValue);

        btnStart = (Button) m_View.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnStart.getText() == "Start"){
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread,0);
                    btnStart.setText("Stop");
                }
                else if(btnStart.getText() == "Stop"){
                    customHandler.removeCallbacks(updateTimerThread);
                    btnStart.setText("Reset");
                }
                else {
                    btnStart.setText("Start");
                    btnPause.setText("Pause");
                    txtTimer.setText("00:00:00:00");
                    startTime = 0L;
                    timeInMilliseconds = 0L;
                    timeSwapBuff = 0L;
                    updateTime = 0L;
                }
            }
        });

        btnPause = (Button) m_View.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnStart.getText()!="Reset") {
                    if (btnPause.getText() == "Pause") {
                        btnPause.setText("Unpause");
                        timeSwapBuff += timeInMilliseconds;
                        customHandler.removeCallbacks(updateTimerThread);
                    } else {
                        btnPause.setText("Pause");
                        startTime = SystemClock.uptimeMillis();
                        customHandler.postDelayed(updateTimerThread, 0);
                    }
                }
            }
        });

        btnLap = (Button) m_View.findViewById(R.id.btnLap); 
        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(btnStart.getText()!="Reset") {
                    LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View addView = inflater.inflate(R.layout.row, null);
                    TextView txtValue = (TextView)addView.findViewById(R.id.txtContent);
                    txtValue.setText(txtTimer.getText());
                    container.addView(addView);
                }*/
            }
        });

    }

    @Override
    public void onTimeUpdated()
    {

    }

    @Override
    public void onDefaultTimeChanged(long p_TimeDiffMS)
    {
    }
}
