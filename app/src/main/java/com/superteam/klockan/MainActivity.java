package com.superteam.klockan;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TimeHandler m_TimeHandler;
    private TabFragment m_TabTime;
    private TabFragment m_TabAlarm;
    private TabFragment m_TabStopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the backgroundService
        Intent backgroundServiceIntent = new Intent(this, BackgroundService.class);
        backgroundServiceIntent.putExtra(BackgroundService.INTENT_EVENT_KEY, BackgroundService.EVENT_START_SERVICE);
        this.startService(backgroundServiceIntent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        m_TabTime = new TabTime();
        m_TabAlarm = new TabAlarm();
        m_TabStopwatch = new TabStopwatch();

        m_TimeHandler = new TimeHandler(new Callback() {
            @Override
            public void onCallback(Object o) {
                m_TabTime.onTimeUpdated();
                m_TabAlarm.onTimeUpdated();
                m_TabStopwatch.onTimeUpdated();
            }
        });

        Preferences.setDefaultTimeChangedCallback(new Callback() {
            @Override
            public void onCallback(Object o) {
                long timeDiffMS = (long) o;
                m_TabTime.onDefaultTimeChanged(timeDiffMS);
                m_TabAlarm.onDefaultTimeChanged(timeDiffMS);
                m_TabStopwatch.onDefaultTimeChanged(timeDiffMS);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Fragment fragment = null;

            switch(position)
            {
                case 0:
                    fragment = m_TabTime;
                    break;
                case 1:
                    fragment = m_TabAlarm;
                    break;
                case 2:
                    fragment = m_TabStopwatch;
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Time";
                case 1:
                    return "Alarm";
                case 2:
                    return "Stopwatch";
            }
            return null;
        }
    }
}
