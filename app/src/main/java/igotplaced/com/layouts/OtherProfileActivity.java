package igotplaced.com.layouts;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.Fragments.OtherProfileEventsFragment;
import igotplaced.com.layouts.Fragments.OtherProfileInterviewFragment;
import igotplaced.com.layouts.Fragments.OtherProfilePostFragment;
import igotplaced.com.layouts.Fragments.OtherProfileQuestionsFragment;
import igotplaced.com.layouts.Fragments.ProfileEventsFragment;
import igotplaced.com.layouts.Fragments.ProfileInterviewExperienceFragment;
import igotplaced.com.layouts.Fragments.ProfilePostFragment;
import igotplaced.com.layouts.Fragments.ProfileQuestionFragment;
import igotplaced.com.layouts.Model.ProfileHome;
import igotplaced.com.layouts.Utils.ConnectivityReceiver;
import igotplaced.com.layouts.Utils.MyApplication;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

public class OtherProfileActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {



    private RequestQueue queue;
    private NetworkImageView profile_img;
    private TextView userProfileName, userProfileDepartment, userProfileCollege;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String otherProfileID;

    private int[] tabIcons = {
            R.drawable.ic_mail_outline_white_36dp,
            R.drawable.ic_timeline_white_24dp,
            R.drawable.ic_event_white_24dp,
            R.drawable.ic_forum_white_24dp
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        initialization();


        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        queue = NetworkController.getInstance(OtherProfileActivity.this).getRequestQueue();



        userProfileName = (TextView) findViewById(R.id.user_profile_name);
        userProfileDepartment = (TextView) findViewById(R.id.user_profile_department);
        userProfileCollege = (TextView) findViewById(R.id.user_profile_college);
        profile_img = (NetworkImageView) findViewById(R.id.user_profile_photo);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        });
        makeJsonArrayRequestProfile();


    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected){
            Utils.showDialogue(OtherProfileActivity.this, "Sorry! Not connected to internet");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(OtherProfileActivity.this);
        if (screenSize(OtherProfileActivity.this) > 8.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initialization() {
        Intent intent = getIntent();
        otherProfileID = intent.getStringExtra("post_createdid");

    }

    private void makeJsonArrayRequestProfile() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/profileService/profile/" + otherProfileID, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);


                        ProfileHome profileHome = new ProfileHome(obj.getString("imgname"), obj.getString("fname"), obj.getString("department"), obj.getString("college"));

                        userProfileName.setText(profileHome.getProfileName());
                        userProfileDepartment.setText(profileHome.getDepartmentName());
                        userProfileCollege.setText(profileHome.getCollegeName());

                        Log.d("error", "Error: " + Utils.BaseImageUri + profileHome.getImageName());
                        profile_img.setImageUrl(Utils.BaseImageUri + profileHome.getImageName(), NetworkController.getInstance(OtherProfileActivity.this).getImageLoader());


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void setupTabIcons() {

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setIcon(tabIcons[i]);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        OtherProfilePostFragment otherProfilePostFragment=new OtherProfilePostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("otherId", otherProfileID);
        otherProfilePostFragment.setArguments(bundle);

        OtherProfileInterviewFragment otherProfileInterviewFragment=new OtherProfileInterviewFragment();
        Bundle bundleInterview = new Bundle();
        bundleInterview.putString("otherId", otherProfileID);
        otherProfileInterviewFragment.setArguments(bundleInterview);

        OtherProfileEventsFragment otherProfileEventsFragment=new OtherProfileEventsFragment();
        Bundle bundleEvents = new Bundle();
        bundleEvents.putString("otherId", otherProfileID);
        otherProfileEventsFragment.setArguments(bundleEvents);

        OtherProfileQuestionsFragment otherProfileQuestionsFragment=new OtherProfileQuestionsFragment();
        Bundle bundleQuestions = new Bundle();
        bundleQuestions.putString("otherId", otherProfileID);
        otherProfileQuestionsFragment.setArguments(bundleQuestions);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(otherProfilePostFragment);
        adapter.addFragment(otherProfileInterviewFragment);
        adapter.addFragment(otherProfileEventsFragment);
        adapter.addFragment(otherProfileQuestionsFragment);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
            //return mFragmentTitleList.get(position);
        }
    }
}
