package igotplaced.com.layouts.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.R;


public class ProfileFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private int[] tabIcons = {
            R.drawable.ic_mail_outline_white_36dp,
            R.drawable.ic_mail_outline_white_36dp,
            R.drawable.ic_mail_outline_white_36dp,
            R.drawable.ic_mail_outline_white_36dp
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


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setupTabIcons();
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return view;

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
