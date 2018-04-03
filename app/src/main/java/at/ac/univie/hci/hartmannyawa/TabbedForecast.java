package at.ac.univie.hci.hartmannyawa;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class TabbedForecast extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_forecast);
        String title = "Forecast for " + getIntent().getExtras().getString("city");
        setTitle(title);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed_forecast, menu);
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed_forecast, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            TextView avg = (TextView) rootView.findViewById(R.id.vavg);
            TextView min = (TextView) rootView.findViewById(R.id.vmin);
            TextView max = (TextView) rootView.findViewById(R.id.vmax);
            TextView savg = (TextView) rootView.findViewById(R.id.avg);
            TextView smin = (TextView) rootView.findViewById(R.id.min);
            TextView smax = (TextView) rootView.findViewById(R.id.max);
            TextView day = (TextView) rootView.findViewById(R.id.day);
            TextView lswipe = (TextView) rootView.findViewById(R.id.lswipe);
            TextView rswipe = (TextView) rootView.findViewById(R.id.rswipe);
            lswipe.setText("<-- SWIPE LEFT");
            rswipe.setText("SWIPE RIGHT -->");

            double [] result;
            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");
            DateTimeFormatter germanFmt = fmt.withLocale(Locale.GERMAN);
            String output = null;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1: {
                    result = getActivity().getIntent().getExtras().getDoubleArray("one");
                    dt = dt.plusDays(1);
                    day.setText(fmt.print(dt));
                    avg.setText(String.valueOf(result[0]).substring(0,5));
                    min.setText(String.valueOf(result[1]).substring(0,5));
                    max.setText(String.valueOf(result[2]).substring(0,5));
                    lswipe.setText("");
                    break;
                }
                case 2: {
                    result = getActivity().getIntent().getExtras().getDoubleArray("two");
                    dt = dt.plusDays(2);
                    day.setText(fmt.print(dt));
                    avg.setText(String.valueOf(result[0]).substring(0,5));
                    min.setText(String.valueOf(result[1]).substring(0,5));
                    max.setText(String.valueOf(result[2]).substring(0,5));
                    break;
                }
                case 3: {
                    result = getActivity().getIntent().getExtras().getDoubleArray("three");
                    dt = dt.plusDays(3);
                    day.setText(fmt.print(dt));
                    avg.setText(String.valueOf(result[0]).substring(0,5));
                    min.setText(String.valueOf(result[1]).substring(0,5));
                    max.setText(String.valueOf(result[2]).substring(0,5));
                    break;
                }
                case 4: {
                    result = getActivity().getIntent().getExtras().getDoubleArray("four");
                    dt = dt.plusDays(4);
                    day.setText(fmt.print(dt));
                    avg.setText(String.valueOf(result[0]).substring(0,5));
                    min.setText(String.valueOf(result[1]).substring(0,5));
                    max.setText(String.valueOf(result[2]).substring(0,5));
                    break;
                }
                case 5: {
                    result = getActivity().getIntent().getExtras().getDoubleArray("five");
                    dt = dt.plusDays(5);
                    day.setText(fmt.print(dt));
                    avg.setText(String.valueOf(result[0]).substring(0,5));
                    min.setText(String.valueOf(result[1]).substring(0,5));
                    max.setText(String.valueOf(result[2]).substring(0,5));
                    rswipe.setText("");
                    break;
                }

            }
            savg.setText(getString(R.string.savg));
            smin.setText(getString(R.string.smin));
            smax.setText(getString(R.string.smax));
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DAY 1";
                case 1:
                    return "DAY 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
