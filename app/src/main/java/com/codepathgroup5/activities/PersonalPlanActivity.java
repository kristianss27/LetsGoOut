package com.codepathgroup5.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepathgroup5.fragments.FbFriendsFragment;
import com.codepathgroup5.fragments.PersonalListFragment;
import com.codepathgroup5.models.Message;
import com.codepathgroup5.utilities.Utility;
import com.codepathgroup5.wanttoknow.MainActivity;
import com.codepathgroup5.wanttoknow.R;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalPlanActivity extends AppCompatActivity {
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvPlanDescription) TextView tvPlanDescription;
    @BindView(R.id.tvDate) TextView tvDate;

    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(getResources().getString(R.string.list_created));
        setSupportActionBar(toolbar);

        //Get the view pager and set its PageAdapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        //Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(viewPager);

        message = Parcels.unwrap(getIntent().getParcelableExtra("message"));
        tvTitle.setText(message.getPlanPurpouse());
        tvPlanDescription.setText(message.getDescription());

        Date when = message.getWhenDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(when);
        String date = new Utility().getFormatDate(calendar, true);
        tvDate.setText(date);

    }

    // METHODS to set up the tool bar and its menu
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                logOut();
                return true;
            case R.id.myPlans:
                myPlans();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void myPlans(){
        Intent intent = new Intent(this, PlansActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void logOut(){
        ParseUser.logOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Returns the fragment according to the tab name in the view pager
    public class PagerAdapter extends FragmentPagerAdapter {
            //implements PagerSlidingTabStrip.IconTabProvider{
        final int PAGE_COUNT = 1;
        //private int tabIcons[] = {R.mipmap.ic_location, R.mipmap.ic_location, R.mipmap.ic_location};
        private String tabTitles[] = new String[] { "You have chosen this from Yelp"};
        //private String tabTitles[] = new String[] { "Your Selection", "Friends"};

        //We should create the constructor to get the FragmentManager
        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        //And Override the methods that allow us to get how many tabs we have and wich Fragment will show off with them

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return new PersonalListFragment();
            }
            else if (position==1){
                return new FbFriendsFragment();
            }
            else{
                return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
/*
        @Override
        public int getPageIconResId(int position) {
            return tabIcons[position];
        }
        */
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
