package igotplaced.com.layouts.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import igotplaced.com.layouts.AddUserExperience;
import igotplaced.com.layouts.AddUserInterviewExperience;
import igotplaced.com.layouts.AddUserQuestions;
import igotplaced.com.layouts.EditProfileActivity;
import igotplaced.com.layouts.Model.ProfileHome;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.RegisterActivity;
import igotplaced.com.layouts.RegisterPasswordActivity;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Email;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private RequestQueue queue;


    private NetworkImageView profile_img;
    private TextView userProfileName, userProfileDepartment, userProfileCollege;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private String postNew, industryNew, companyNew;
    private String interviewNew, placedData, industryPlaced, companyPlaced;
    private String  questionsNew,industryQuestion,companyQuestion;

    private boolean fabStatus = false;
    private FrameLayout fraToolFloat;
    private FloatingActionButton fabSetting;
    private FloatingActionButton fabSub1;
    private FloatingActionButton fabSub2;
    private FloatingActionButton fabSub3;


    private LinearLayout linFabSetting;
    private LinearLayout linFab1;
    private LinearLayout linFab2;
    private LinearLayout linFab3;
    boolean flag = true;


    private String URL = BaseUri + "/profileService/profilePost";
    public static final int POST_REQUEST_CODE = 1;
    public static final int INTERVIEW_REQUEST_CODE = 11;
    public static final int QUESTIONS_REQUEST_CODE = 111;

    private int[] tabIcons = {
            R.drawable.ic_mail_outline_white_36dp,
            R.drawable.ic_timeline_white_24dp,
            R.drawable.ic_event_white_24dp,
            R.drawable.ic_forum_white_24dp
    };

    private SharedPreferences sharedpreferences;
    private String userName = null, userId = null, userEmail;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       /* if (container != null) {
            container.removeAllViews();
        }*/

        context = getActivity().getApplicationContext();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("Profile");

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        setupViewPager(viewPager);


        queue = NetworkController.getInstance(context).getRequestQueue();


        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);
        userEmail = sharedpreferences.getString(Email, null);


        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);


        userProfileName = (TextView) view.findViewById(R.id.user_profile_name);
        userProfileDepartment = (TextView) view.findViewById(R.id.user_profile_department);
        userProfileCollege = (TextView) view.findViewById(R.id.user_profile_college);
        profile_img = (NetworkImageView) view.findViewById(R.id.user_profile_photo);

        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestProfile();


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();

            }

        });
        fab(view);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        fabSetting.setImageResource(R.drawable.ic_add_white_24dp);
        hideFab();

    }

    private void fab(View view) {
        fraToolFloat = (FrameLayout) view.findViewById(R.id.fraToolFloat);
        fabSetting = (FloatingActionButton) view.findViewById(R.id.fabSetting);
        fabSub1 = (FloatingActionButton) view.findViewById(R.id.fabSub1);
        fabSub2 = (FloatingActionButton) view.findViewById(R.id.fabSub2);
        fabSub3 = (FloatingActionButton) view.findViewById(R.id.fabSub3);

        linFab1 = (LinearLayout) view.findViewById(R.id.linFab1);
        linFab2 = (LinearLayout) view.findViewById(R.id.linFab2);
        linFab3 = (LinearLayout) view.findViewById(R.id.linFab3);

        linFabSetting = (LinearLayout) view.findViewById(R.id.linFabSetting);

        fabSub1.setOnClickListener(this);
        fabSub2.setOnClickListener(this);
        fabSub3.setOnClickListener(this);

        fabSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabStatus) {
                    if (flag) {
                        fabSetting.setImageResource(R.drawable.ic_add_white_24dp);
                        flag = false;
                        hideFab();
                    }
                } else {
                    fabSetting.setImageResource(R.drawable.ic_clear_white_24dp);
                    flag = true;
                    showFab();
                }
            }
        });

    }

    private void hideFab() {
        linFab1.setVisibility(View.INVISIBLE);
        linFab2.setVisibility(View.INVISIBLE);
        linFab3.setVisibility(View.INVISIBLE);
        fraToolFloat.setBackgroundColor(Color.argb(0, 255, 255, 255));
        fabStatus = false;
    }

    private void showFab() {
        linFab1.setVisibility(View.VISIBLE);
        linFab2.setVisibility(View.VISIBLE);
        linFab3.setVisibility(View.VISIBLE);
        linFab3.setVisibility(View.VISIBLE);
        fraToolFloat.setBackgroundColor(Color.argb(200, 0, 0, 0));
        fabStatus = true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fabSub1:
                addNewPost();
                break;
            case R.id.fabSub2:
                addInterviewExperience();
                break;
            case R.id.fabSub3:
                addQuestionsNew();
                break;
        }

    }

    private void addQuestionsNew() {
        Intent popupAddIntent = new Intent(context, AddUserQuestions.class);
        startActivityForResult(popupAddIntent, QUESTIONS_REQUEST_CODE);
    }

    private void addInterviewExperience() {
        Intent popupAddIntent = new Intent(context, AddUserInterviewExperience.class);
        startActivityForResult(popupAddIntent, INTERVIEW_REQUEST_CODE);
    }

    private void addNewPost() {
        Intent popupAddIntent = new Intent(context, AddUserExperience.class);
        startActivityForResult(popupAddIntent, POST_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == POST_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                postNew = data.getStringExtra("postNew");
                industryNew = data.getStringExtra("industry1");
                companyNew = data.getStringExtra("company1");

                Toast.makeText(getContext(),"Thanks for Sharing",Toast.LENGTH_SHORT).show();
                addPostDb();

            } else if (resultCode == Activity.RESULT_CANCELED) {
               // Toast.makeText(context, "User Cancelled", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == INTERVIEW_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                interviewNew = data.getStringExtra("interviewNew");
                placedData = data.getStringExtra("interest");
                industryPlaced = data.getStringExtra("industry2");
                companyPlaced = data.getStringExtra("company2");
                Toast.makeText(getContext(),"Thanks for Sharing",Toast.LENGTH_SHORT).show();
                addInterviewDb();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                //Toast.makeText(context, "User Cancelled", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == QUESTIONS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                 questionsNew = data.getStringExtra("questionsNew");
                 industryQuestion = data.getStringExtra("industry3");
                 companyQuestion = data.getStringExtra("company3");
                Toast.makeText(getContext(),"Thanks for Sharing",Toast.LENGTH_SHORT).show();
                addQuestionsDb();
            } else if (resultCode == Activity.RESULT_CANCELED) {
               // Toast.makeText(context, "User Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addQuestionsDb() {
        StringRequest request = new StringRequest(Request.Method.POST, BaseUri + "/profileService/profileQuestions", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(getActivity(), "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("question", questionsNew);
                parameters.put("created_user", userId);
                parameters.put("industryname", industryQuestion);
                parameters.put("companyname", companyQuestion.replaceAll(",$", ""));
                parameters.put("created_uname", userProfileName.getText().toString());



                Log.e("QuesAdd", "" + companyQuestion.replaceAll(",$", ""));
                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 30000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);
    }

    private void addInterviewDb() {

        StringRequest request = new StringRequest(Request.Method.POST, BaseUri + "/profileService/profileInterview", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(getActivity(), "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("feedback", interviewNew);
                parameters.put("user_id", userId);
                parameters.put("industryname", industryPlaced);
                parameters.put("companyname", companyPlaced.replaceAll(",$", ""));
                parameters.put("username", userProfileName.getText().toString());
                parameters.put("interview_status",String.valueOf((Boolean.parseBoolean(placedData)) ? 1 : 0));


                Log.e("InterAdded", "" + companyPlaced.replaceAll(",$", ""));
                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 30000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);
    }

    private void addPostDb() {


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(getActivity(), "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("post", postNew);
                parameters.put("Industry", industryNew);
                parameters.put("created_user", userId);
                parameters.put("company1", companyNew.replaceAll(",$", ""));
                parameters.put("created_uname", userProfileName.getText().toString());


                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 30000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

    }

    private void makeJsonArrayRequestProfile() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/profileService/profile/" + userId, new Response.Listener<JSONArray>() {

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
                        profile_img.setImageUrl(Utils.BaseImageUri + profileHome.getImageName(), NetworkController.getInstance(context).getImageLoader());


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
                Utils.showDialogue(getActivity(), "Sorry! Server Error");
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ProfilePostFragment());
        adapter.addFragment(new ProfileInterviewExperienceFragment());
        adapter.addFragment(new ProfileEventsFragment());
        adapter.addFragment(new ProfileQuestionFragment());
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
