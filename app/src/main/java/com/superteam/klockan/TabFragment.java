package com.superteam.klockan;

import android.support.v4.app.Fragment;

/**
 * Created by Rasmus on 2016-12-30.
 */

public abstract class TabFragment extends Fragment
{
    public abstract void onTimeUpdated();

    public abstract void onDefaultTimeChanged(long p_TimeDiffMS);
}
