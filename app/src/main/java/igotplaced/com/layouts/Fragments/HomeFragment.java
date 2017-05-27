package igotplaced.com.layouts.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterMentorsHome;
import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterRecentFeedsHome;
import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterRecentlyGotPlacedHome;
import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterTestimonialsHome;
import igotplaced.com.layouts.MainHomeActivity;
import igotplaced.com.layouts.Model.MentorsHome;
import igotplaced.com.layouts.Model.RecentFeedsHome;
import igotplaced.com.layouts.Model.RecentlyGotPlacedHome;
import igotplaced.com.layouts.Model.TestimonialsHome;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;


public class HomeFragment extends Fragment implements View.OnClickListener {


    private Context context;
    private RequestQueue queue;
    private List<RecentFeedsHome> recentFeedsHomeList = new ArrayList<RecentFeedsHome>();
    private RecyclerAdapterRecentFeedsHome recyclerAdapterRecentFeedsHome;

    private View view;

    private List<MentorsHome> mentorsHomeList = new ArrayList<MentorsHome>();
    private RecyclerAdapterMentorsHome recyclerAdapterMentorsHome;

    private List<RecentlyGotPlacedHome> recentlyGotPlacedList = new ArrayList<RecentlyGotPlacedHome>();
    private RecyclerAdapterRecentlyGotPlacedHome recyclerAdapterRecentlyGotPlacedHome;

    private List<TestimonialsHome> testimonialsList = new ArrayList<TestimonialsHome>();
    private RecyclerAdapterTestimonialsHome recyclerAdapterTestimonialsHome;

    private FragmentManager fragmentManager;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        //getting context
        context = getActivity().getApplicationContext();

        settingRecyclerView(view);

        return view;
    }

    private void settingRecyclerView(View view) {

        //mapping RecyclerView
        RecyclerView my_recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view_feed);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterRecentFeedsHome = new RecyclerAdapterRecentFeedsHome(context, recentFeedsHomeList);

        //setting fixed size
        my_recycler_view.setHasFixedSize(true);
        //setting horizontal layout
        my_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //setting RecyclerView adapter
        my_recycler_view.setAdapter(recyclerAdapterRecentFeedsHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestRecentFeeds();


        //mapping RecyclerView
        RecyclerView my_mentor_view = (RecyclerView) view.findViewById(R.id.recycler_view_mentors);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterMentorsHome = new RecyclerAdapterMentorsHome(context, mentorsHomeList);

        //setting fixed size
        my_mentor_view.setHasFixedSize(true);
        //setting horizontal layout
        my_mentor_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //setting RecyclerView adapter
        my_mentor_view.setAdapter(recyclerAdapterMentorsHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestMentorsHome();


        //mapping RecyclerView
        RecyclerView recently_placed_view = (RecyclerView) view.findViewById(R.id.recycler_view_recently_got_placed);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterRecentlyGotPlacedHome = new RecyclerAdapterRecentlyGotPlacedHome(context, recentlyGotPlacedList);

        //setting fixed size
        recently_placed_view.setHasFixedSize(true);
        //setting horizontal layout
        recently_placed_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //setting RecyclerView adapter
        recently_placed_view.setAdapter(recyclerAdapterRecentlyGotPlacedHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestRecentlyGotPlaced();


        //mapping RecyclerView
        RecyclerView testimonials_view = (RecyclerView) view.findViewById(R.id.recycler_view_testimonials);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterTestimonialsHome = new RecyclerAdapterTestimonialsHome(context, testimonialsList);

        //setting fixed size
        testimonials_view.setHasFixedSize(true);
        //setting horizontal layout
        testimonials_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //setting RecyclerView adapter
        testimonials_view.setAdapter(recyclerAdapterTestimonialsHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestTestimonials();


        Button postButton = (Button) view.findViewById(R.id.postButtonHome);
        postButton.setOnClickListener(this);

        Button interviewButton = (Button) view.findViewById(R.id.interviewButtonHome);
        interviewButton.setOnClickListener(this);

        Button eventButton = (Button) view.findViewById(R.id.eventsButtonHome);
        eventButton.setOnClickListener(this);

        Button questionsButton = (Button) view.findViewById(R.id.questionsButtonHome);
        questionsButton.setOnClickListener(this);
    }



    private void makeJsonArrayRequestTestimonials() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/homeService/testimonials", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        TestimonialsHome testimonials = new TestimonialsHome(obj.getString("feedback"), obj.getString("user_name"),obj.getString("college"), obj.getString("imgname"));
                        // adding movie to testimonialsList array
                        testimonialsList.add(testimonials);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterTestimonialsHome.notifyDataSetChanged();
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






    private void makeJsonArrayRequestRecentlyGotPlaced() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/homeService/recentlyGotPlaced", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        RecentlyGotPlacedHome recentlyGotPlaced = new RecentlyGotPlacedHome(obj.getString("imgname"), obj.getString("username"), obj.getString("feedback"));
                        // adding movie to recentlyGotPlacedList array
                        recentlyGotPlacedList.add(recentlyGotPlaced);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterRecentlyGotPlacedHome.notifyDataSetChanged();
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


    private void makeJsonArrayRequestMentorsHome() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/homeService/mentors", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        MentorsHome mentorsHome = new MentorsHome(obj.getString("imgname"), obj.getString("name"), obj.getString("designation"), obj.getString("company"), obj.getString("linkedin"));
                        // adding movie to mentorsHomeList array
                        mentorsHomeList.add(mentorsHome);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterMentorsHome.notifyDataSetChanged();
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


    private void makeJsonArrayRequestRecentFeeds() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/homeService/recentFeeds", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        RecentFeedsHome recentFeedsHome = new RecentFeedsHome(obj.getString("type"), obj.getString("question"), obj.getString("industryname"), obj.getString("companyname"), obj.getString("modified_by"), obj.getString("name"), obj.getString("imgname"));
                        // adding movie to recentFeedsHomeList array
                        recentFeedsHomeList.add(recentFeedsHome);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterRecentFeedsHome.notifyDataSetChanged();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View v) {

        fragmentManager = getFragmentManager();
        switch (v.getId()) {
            case R.id.postButtonHome:

                Intent postIntent = new Intent(getActivity(), MainHomeActivity.class);
                postIntent.putExtra("bottomNavigation",1);
                startActivity(postIntent);
                getActivity().overridePendingTransition(0,0);

                break;
            case R.id.interviewButtonHome:

                Intent interviewIntent = new Intent(getActivity(), MainHomeActivity.class);
                interviewIntent.putExtra("bottomNavigation",2);
                startActivity(interviewIntent);
                getActivity().overridePendingTransition(0,0);
                break;
            case R.id.eventsButtonHome:

                Intent eventIntent = new Intent(getActivity(), MainHomeActivity.class);
                eventIntent.putExtra("bottomNavigation",3);
                startActivity(eventIntent);
                getActivity().overridePendingTransition(0,0);
                break;
            case R.id.questionsButtonHome:

                Intent questionIntent = new Intent(getActivity(), MainHomeActivity.class);
                questionIntent.putExtra("bottomNavigation",4);
                startActivity(questionIntent);
                getActivity().overridePendingTransition(0,0);
                break;
        }
    }
}
