package com.superteam.klockan;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by William on 2017-01-4.
 */

public class TabStopwatch extends TabFragment
{
    private View m_View;
    private String intNumbers[] = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14",
                        "15","16","17","18","19"};
    private String txtSingleNumber[] = {"zero","one","two","three","four","five","six","seven","eight","nine",
                            "ten","eleven","twelve","thirteen","fourteen","fifteen","sixteen","seventeen",
                            "eighteen","nineteen"};
    private String txtTenNumbers[] = {"","","twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    private Button btnStart, btnPause, btnLap;
    private TextView txtTimer;
    private Handler customHandler = new Handler();

    private ArrayList<String> arrayList;
    private ListView listView;
    private ArrayAdapter adapter;

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
            secs%=60;
            txtTimer.setText(String.format("%02d",mins)+":"
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
        txtTimer = (TextView)m_View.findViewById(R.id.txtTimeValue);

        listView = (ListView) m_View.findViewById(R.id.listLap);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(m_View.getContext(), R.layout.row, R.id.txtContent, arrayList);
        listView.setAdapter(adapter);

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
                    txtTimer.setText(toText(txtTimer.getText().toString()));
                    btnStart.setText("Reset");
                }
                else {
                    btnStart.setText("Start");
                    btnPause.setText("Pause");
                    txtTimer.setText("00:00:00");
                    startTime = 0L;
                    timeInMilliseconds = 0L;
                    timeSwapBuff = 0L;
                    updateTime = 0L;
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        btnPause = (Button) m_View.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnStart.getText()!= "Reset" && btnStart.getText() != "Start") {
                    if (btnPause.getText() == "Pause") {
                        btnPause.setText("Unpause");
                        timeSwapBuff += timeInMilliseconds;
                        txtTimer.setText(toText(txtTimer.getText().toString()));
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
                if(btnStart.getText()!= "Reset" && btnStart.getText() != "Start" && btnPause.getText() != "Unpause") {
                    arrayList.add(0, toText(txtTimer.getText().toString()));
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    protected String toText(String txt){
        String txtString[] = txt.split(":");
        int checker = 0;
        for(int i = 0; i < 3; i++){     //loop 3 times to get hours/mins/secs/millisecs
            for(int j = 0; j < 20; j++){ //loop checks between 00-19
                if(txtString[i].equals(intNumbers[j])){
                    txtString[i] = txtSingleNumber[j];
                    checker = 1;
                    j=20;
                }
            }
            if(checker != 1) {
                String nr[] = new String[2];
                int tmp;
                tmp = Integer.parseInt(txtString[i]);
                nr[1] = ""+tmp%10;
                nr[0] = ""+(tmp - Integer.parseInt(nr[1]))/10;
                nr[1] = "0"+nr[1];
                nr[0] = "0"+nr[0];

                for(int j = 2; j < 10; j++){
                    if(nr[0].equals(intNumbers[j])){
                        txtString[i]=txtTenNumbers[j];
                        j=10;
                    }
                }
                if(nr[1].equals("00")) {
                    for (int j = 0; j < 10; j++) {
                        if (nr[1].equals(intNumbers[j])) {
                            txtString[i] += "-" + txtSingleNumber[j];
                            j = 10;
                        }
                    }
                }
            }
            checker = 0;
        }
        return txtString[0]+":"+txtString[1]+":"+txtString[2];
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
