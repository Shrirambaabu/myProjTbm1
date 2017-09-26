package igotplaced.com.layouts;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.Fragments.CompanyAboutFragment;
import igotplaced.com.layouts.Fragments.CompanyEventFragment;
import igotplaced.com.layouts.Fragments.CompanyInterviewFragment;
import igotplaced.com.layouts.Fragments.CompanyPostFragment;
import igotplaced.com.layouts.Fragments.CompanyQuestionFragment;
import igotplaced.com.layouts.Fragments.OtherProfilePostFragment;
import igotplaced.com.layouts.Model.Company;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.Utils.ConnectivityReceiver;
import igotplaced.com.layouts.Utils.MyApplication;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.isColorDark;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

public class CompanyDetailsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {


    private RequestQueue queue;

    private TextView  companyWebsite;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView companyImage;
    private AppBarLayout appBarLayout;
    private int[] tabIcons = {
            R.drawable.ic_mail_outline_white_36dp,
            R.drawable.ic_timeline_white_24dp,
            R.drawable.ic_event_white_24dp,
            R.drawable.ic_forum_white_24dp,
            R.drawable.ic_info_outline_white_24dp
    };

    private String companyNameIntent;
    private String companyId;
    private Drawable navDrawable,drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        setTheme(R.style.AppTheme);
        queue = NetworkController.getInstance(CompanyDetailsActivity.this).getRequestQueue();


        initialization();


        companyWebsite = (TextView) findViewById(R.id.company_profile_website);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        companyImage = (ImageView) findViewById(R.id.header_image);

        setupToolbar();
        setupCollapsingToolbar();


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);


        // companyName = (TextView) findViewById(R.id.company_profile_name);

        // profile_img = (NetworkImageView) findViewById(R.id.company_profile_photo);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        });


        makeJsonArrayRequestCompany();


    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(companyNameIntent);
        }
    }

    private void setupCollapsingToolbar() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                navDrawable = toolbar.getNavigationIcon();
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(CompanyDetailsActivity.this, R.color.colorPrimaryDark));
                    companyWebsite.setVisibility(View.GONE);
                    if (navDrawable != null) {
                        navDrawable.mutate();
                        navDrawable.setColorFilter(ContextCompat.getColor(CompanyDetailsActivity.this, R.color.white), PorterDuff.Mode.SRC_ATOP);
                    }


                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(CompanyDetailsActivity.this, android.R.color.transparent));

                }

            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Utils.showDialogue(CompanyDetailsActivity.this, "Sorry! Not connected to internet");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(CompanyDetailsActivity.this);

        if (screenSize(CompanyDetailsActivity.this) > 8.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void makeJsonArrayRequestCompany() {

        Log.e("Company URL", "" + BaseUri + "/profileService/companyDetails/" + companyId);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/profileService/companyDetails/" + companyId, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);


                        Company company = new Company(obj.getString("id"), obj.getString("companyname"), obj.getString("companywebsite"), obj.getString("companyImage"), obj.getString("aboutus"));

                        // companyName.setText(company.getCompanyName());

                        final String companyWebsiteName = company.getCompanyWebsite().replaceFirst("^(http://www\\.|http://|www\\.)", "");


                        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
                        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
                        collapsingToolbarLayout.setTitleEnabled(true);
                        collapsingToolbarLayout.setTitle(company.getCompanyName());

                        companyWebsite.setText(companyWebsiteName);
                        //  profile_img.setImageUrl(Utils.BaseImageUri +company.getCompanyImage() , NetworkController.getInstance(context).getImageLoader());
                        String imgUrl = Utils.BaseImageUri + company.getCompanyImage();

                        Glide.with(CompanyDetailsActivity.this)
                                .load(imgUrl)
                                .asBitmap()

                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        companyImage.setImageBitmap(resource);
                                        changeBasedOnImage(companyImage);
                                    }
                                });
                        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                            int scrollRange = -1;

                            @Override
                            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                                navDrawable = toolbar.getNavigationIcon();
                                //Check if the view is collapsed
                                if (scrollRange + verticalOffset == 0) {
                                    toolbar.setBackgroundColor(ContextCompat.getColor(CompanyDetailsActivity.this, R.color.colorPrimaryDark));
                                    companyWebsite.setVisibility(View.GONE);
                                    if (navDrawable != null) {
                                        navDrawable.mutate();
                                        navDrawable.setColorFilter(ContextCompat.getColor(CompanyDetailsActivity.this, R.color.white), PorterDuff.Mode.SRC_ATOP);
                                    }

                                } else {
                                    toolbar.setBackgroundColor(ContextCompat.getColor(CompanyDetailsActivity.this, android.R.color.transparent));

                                }

                            }
                        });
                        companyWebsite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse("https://www." + companyWebsiteName);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });



                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
                Utils.showDialogue(CompanyDetailsActivity.this, "Sorry! Server Error");
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void changeBasedOnImage(ImageView companyImage) {
        Palette.from(((BitmapDrawable) companyImage.getDrawable()).getBitmap()).maximumColorCount(16).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getDominantSwatch();

                if (vibrant != null) {
                    if (isColorDark(vibrant.getRgb())) {
                        companyWebsite.setTextColor(Color.WHITE);
                        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

                        navDrawable = toolbar.getNavigationIcon();
                        if (navDrawable != null) {
                            navDrawable.mutate();
                            navDrawable.setColorFilter(ContextCompat.getColor(CompanyDetailsActivity.this, R.color.white), PorterDuff.Mode.SRC_ATOP);
                        }

                    } else {
                        companyWebsite.setTextColor(Color.BLACK);
                        collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);

                         drawable = toolbar.getNavigationIcon();
                        if (drawable != null) {
                            drawable.mutate();
                            drawable.setColorFilter(ContextCompat.getColor(CompanyDetailsActivity.this, R.color.black), PorterDuff.Mode.SRC_ATOP);
                        }

                    }
                }
            }
        });

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
        companyNameIntent = intent.getStringExtra("postCompany");
        companyId = intent.getStringExtra("companyId");

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

        CompanyPostFragment companyPostFragment = new CompanyPostFragment();
        Bundle bundlePost = new Bundle();
        bundlePost.putString("otherId", companyId);
        companyPostFragment.setArguments(bundlePost);

        CompanyInterviewFragment companyInterviewFragment = new CompanyInterviewFragment();
        Bundle bundleInterview = new Bundle();
        bundleInterview.putString("otherId", companyId);
        companyInterviewFragment.setArguments(bundleInterview);

        CompanyEventFragment companyEventFragment = new CompanyEventFragment();
        Bundle bundleEvents = new Bundle();
        bundleEvents.putString("otherId", companyId);
        companyEventFragment.setArguments(bundleEvents);

        CompanyQuestionFragment companyQuestionFragment = new CompanyQuestionFragment();
        Bundle bundleQuestion = new Bundle();
        bundleQuestion.putString("otherId", companyId);
        companyQuestionFragment.setArguments(bundleQuestion);

        CompanyAboutFragment companyAboutFragment = new CompanyAboutFragment();
        Bundle bundleAbout = new Bundle();
        bundleAbout.putString("otherId", companyId);
        companyAboutFragment.setArguments(bundleAbout);


        adapter.addFragment(companyPostFragment);
        adapter.addFragment(companyInterviewFragment);
        adapter.addFragment(companyEventFragment);
        adapter.addFragment(companyQuestionFragment);
        adapter.addFragment(companyAboutFragment);
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
