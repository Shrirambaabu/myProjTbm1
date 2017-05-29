package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.CustomAdapter.AdapterProfileHome;
import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterNotification;
import igotplaced.com.layouts.EditProfileActivity;
import igotplaced.com.layouts.Model.NotificationView;
import igotplaced.com.layouts.Model.ProfileHome;
import igotplaced.com.layouts.R;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;


public class ProfileFragment extends Fragment {

    private Context context;
    private RequestQueue queue;
    private List<ProfileHome> profileViewList = new ArrayList<ProfileHome>();
    private AdapterProfileHome adapterProfileHome;


    private SwipeRefreshLayout swipeRefreshLayout;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private int[] tabIcons = {
            R.drawable.ic_mail_outline_white_36dp,
            R.drawable.ic_timeline_white_24dp,
            R.drawable.ic_event_white_24dp,
            R.drawable.ic_forum_white_24dp
    };


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       /* if (container != null) {
            container.removeAllViews();
        }*/
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ImageView profView = (ImageView) view.findViewById(R.id.edit_profile);

        profView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prof = new Intent(getActivity(),EditProfileActivity.class);
                startActivity(prof);
            }
        });

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
                //Volley's inbuilt class to make Json array request
                //  makeJsonArrayRequestProfile();
            }

        });

        return view;

    }

    private void makeJsonArrayRequestProfile() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/profileService/profile/256", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                //clearing notificationList
                profileViewList.clear();

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        ProfileHome profView = new ProfileHome(obj.getString("imgname"), obj.getString("fname"), obj.getString("department"), obj.getString("college"));
                        // adding movie to profileList array
                        profileViewList.add(profView);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        adapterProfileHome.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
                swipeRefreshLayout.setRefreshing(false);
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new ProfilePostFragment());
        adapter.addFragment(new ProfileInterviewExperienceFragment());
        adapter.addFragment(new ProfileEventsFragment());
        adapter.addFragment(new ProfileQuestionFragment());
        viewPager.setAdapter(adapter);
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
