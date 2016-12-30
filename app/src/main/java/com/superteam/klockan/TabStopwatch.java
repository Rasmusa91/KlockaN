package com.superteam.klockan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Rasmus on 2016-12-29.
 */

public class TabStopwatch extends TabFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stopwatch_layout, container, false);

        return rootView;
    }

    @Override
    public void onTimeUpdated()
    {

    }
}
