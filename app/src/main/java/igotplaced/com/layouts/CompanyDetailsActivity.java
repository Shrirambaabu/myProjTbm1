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
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.Fragments.OtherProfilePostFragment;
import igotplaced.com.layouts.Utils.NetworkController;

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
        makeJsonArrayRequestCompany();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(companyNameIntent);
        }
    }

    private void makeJsonArrayRequestCompany() {

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
