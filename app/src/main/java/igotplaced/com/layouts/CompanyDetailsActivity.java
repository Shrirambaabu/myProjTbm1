package igotplaced.com.layouts;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.Fragments.OtherProfilePostFragment;
import igotplaced.com.layouts.Model.Company;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;

public class CompanyDetailsActivity extends AppCompatActivity {

    private Context context;
    private RequestQueue queue;
    private NetworkImageView profile_img;
    private TextView companyName, companyWebsite;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private int[] tabIcons = {
            R.drawable.ic_mail_outline_white_36dp,
            R.drawable.ic_timeline_white_24dp,
            R.drawable.ic_event_white_24dp,
            R.drawable.ic_forum_white_24dp,
            R.drawable.ic_info_outline_white_24dp
    };
    private Intent intent;
    private String companyNameIntent;
    private String companyId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        initialization();
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        queue = NetworkController.getInstance(context).getRequestQueue();

        companyName = (TextView) findViewById(R.id.company_profile_name);
        companyWebsite = (TextView) findViewById(R.id.company_profile_website);
        profile_img = (NetworkImageView) findViewById(R.id.company_profile_photo);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        });

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(companyNameIntent);
        }
        makeJsonArrayRequestCompany();
    }

    private void makeJsonArrayRequestCompany() {

        Log.e("Company URL",""+ BaseUri + "/profileService/companyDetails/" + companyId);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/profileService/companyDetails/" + companyId, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);


                        Company company = new Company(obj.getString("id"),obj.getString("companyname"),obj.getString("companywebsite"),obj.getString("companyImage"),obj.getString("aboutus"));

                        companyName.setText(company.getCompanyName());
                        companyWebsite.setText(company.getCompanyWebsite());
                        profile_img.setImageUrl(Utils.BaseImageUri +company.getCompanyImage() , NetworkController.getInstance(context).getImageLoader());


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
        intent = getIntent();
        companyNameIntent= intent.getStringExtra("postCompany");
        companyId= intent.getStringExtra("companyId");
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new OtherProfilePostFragment());
        adapter.addFragment(new OtherProfilePostFragment());
        adapter.addFragment(new OtherProfilePostFragment());
        adapter.addFragment(new OtherProfilePostFragment());
        adapter.addFragment(new OtherProfilePostFragment());
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
