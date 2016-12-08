package com.codepathgroup5.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepathgroup5.adapters.PlanAdapter;
import com.codepathgroup5.fragments.MyPlansFragment;
import com.codepathgroup5.fragments.QuestionFragment;
import com.codepathgroup5.fragments.QuestionaryFragment;
import com.codepathgroup5.fragments.ThePlanFragment;
import com.codepathgroup5.models.PlanPO;
import com.codepathgroup5.wanttoknow.MainActivity;
import com.codepathgroup5.wanttoknow.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class PlansActivity extends AppCompatActivity implements PlanAdapter.AdapterListener, MyPlansFragment.MyPlansListener, QuestionaryFragment.QuestionaryFragmentListener {
    private static final String TAG = "PlansActivity";
    private MyPlansFragment myPlansFragment;
    private QuestionFragment questionFragment;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private PlanPO planPO;
    private String planId;
    private Toolbar toolbar;
    private ParseUser parseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        //Getting the current user
        parseUser = ParseUser.getCurrentUser();


        if (getIntent().getStringExtra("plan_id") != null) {
            planId = getIntent().getStringExtra("plan_id");

            ParseQuery<PlanPO> planQuery = ParseQuery.getQuery(PlanPO.class);
            planQuery.whereEqualTo("objectId", planId);
            planQuery.include("yelp_list");
            planQuery.findInBackground(new FindCallback<PlanPO>() {
                @Override
                public void done(List<PlanPO> list, ParseException e) {
                    if (e == null) {
                        final int size = list.size();
                        Log.d("LIST SIZE", " List size start in:" + size);
                        if (size > 0) {
                            planPO = list.get(0);
                            //if its a notification we are going to call our ThePlanFragment
                            if (planPO != null) {
                                getSupportFragmentManager().beginTransaction().
                                        replace(R.id.frame_layout, ThePlanFragment.newInstance(planPO), "PLAN").
                                        commit();
                            } else {
                                getSupportFragmentManager().beginTransaction().
                                        replace(R.id.frame_layout, new MyPlansFragment(false), "MYPLANS").
                                        commit();
                            }
                        }
                    } else {
                        Log.e("message", "Error Loading Messages" + e);
                    }
                }
            });
        }

        //Set up the toolbar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("My plans");
        setSupportActionBar(toolbar);

        //Getting the Drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();


        //Getting the navigation view
        navigationView = (NavigationView) findViewById(R.id.nvView);
        //Set up drawer content
        setupDrawerContent(navigationView);

        // Inflate the header view at runtime
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        // We can now look up items within the header if needed
        TextView tvUserName = (TextView) headerLayout.findViewById(R.id.tvUserName);
        tvUserName.setText(parseUser.getString("name"));
        final ImageView imageProfile = (ImageView) headerLayout.findViewById(R.id.imgProfileHeader);
        imageProfile.setImageResource(0);
        //Add image profile to the header
        String facebookId = parseUser.getString("facebook_id");
        String pictureUrl="https://graph.facebook.com/"+facebookId+"/picture";

        Glide.with(imageProfile.getContext())
                .load(pictureUrl)
                .asBitmap()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .centerCrop()
                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .override(160, 160)
                .into(new BitmapImageViewTarget(imageProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(imageProfile.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });

        //Adding dynamically a fragment into a frame container
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frame_layout, new MyPlansFragment(false), "MYPLANS").
                commit();
        // We can look up the fragment by tag
        //myPlansFragment = (MyPlansFragment) getSupportFragmentManager().findFragmentByTag("MYPLANS");

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.addPlan:
                addPlan();
                break;
            case R.id.myPlansMain:
                myPlans();
                break;
            case R.id.logout:
                logOut();
                break;
            case R.id.myPlans:
                myPlans();
                break;
            case R.id.friendsPlans:
                friendsPlans();
                break;
            default:
                myPlans();
        }

        // Highlight the selected item has been done by NavigationView
        //menuItem.setChecked(true);
        // Set action bar title
        toolbar.setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }


    // METHODS to set up the tool bar and its menu
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.addPlan:
                addPlan();
                return true;
            case R.id.logout:
                logOut();
                return true;
            case R.id.myPlans:
                myPlans();
                return true;
            case R.id.myPlansMain:
                myPlans();
                return true;
            case R.id.friendsPlans:
                friendsPlans();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void myPlans() {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frame_layout, new MyPlansFragment(false), "MYPLANS").
                commit();
    }

    public void friendsPlans() {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frame_layout, new MyPlansFragment(true), "MYFRIENDSPLANS").
                commit();
    }

    public void logOut() {
        ParseUser.logOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void searchInYelp() {
        Intent intent = new Intent(PlansActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void sendDescription() {

    }

    @Override
    public void addPlan() {
        MyPlansFragment myPlansFragment = (MyPlansFragment)
                getSupportFragmentManager().findFragmentByTag("MYPLANS");

        if (myPlansFragment != null && myPlansFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_layout, QuestionaryFragment.newInstance(), "QUESTIONARY").
                    commit();
        }
    }

    @Override
    public void openThePlan(PlanPO planPO) {
        ThePlanFragment thePlanFragment = (ThePlanFragment)
                getSupportFragmentManager().findFragmentByTag("THEPLAN");

        getSupportFragmentManager().beginTransaction().
                replace(R.id.frame_layout, ThePlanFragment.newInstance(planPO), "PLAN").
                commit();

    }


}
