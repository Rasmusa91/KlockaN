package com.superteam.klockan;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by Rasmus on 2016-12-30.
 */

public class TimeHandler
{
    final int FPS = 25;
    final int TICKS = 1000 / FPS;

    private Handler handler;
    private Runnable runnable = new Runnable() {
        private void sendMessage()
        {
            Bundle bundle = new Bundle();
            bundle.putString("Update", "Update");

            Message message = new Message();
            message.setData(bundle);

            handler.sendMessage(message);
        }

        @Override
        public void run() {
            double next_game_tick = System.currentTimeMillis();

            while (true) {
                while (System.currentTimeMillis() > next_game_tick) {

                    sendMessage();

                    next_game_tick += TICKS;
                }
            }
        }
    };
    private Callback m_Callback;

    public TimeHandler(Callback p_Callback)
    {
        m_Callback = p_Callback;

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.getData().containsKey("Update"))
                {
                    m_Callback.onCallback(null);
                }

                return true;
            }
        });

        new Thread(runnable, "TimeHandlerThread").start();
    }
}
