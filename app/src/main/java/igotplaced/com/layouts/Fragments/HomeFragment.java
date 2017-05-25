package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterRecentFeeds;
import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterRecentlyGotPlaced;
import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterTestimonials;
import igotplaced.com.layouts.Model.MentorsHome;
import igotplaced.com.layouts.Model.RecentFeeds;
import igotplaced.com.layouts.Model.RecentlyGotPlaced;
import igotplaced.com.layouts.Model.Testimonials;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;


public class HomeFragment extends Fragment {


    private Context context;
    private RequestQueue queue;
    private List<RecentFeeds> recentFeedsList = new ArrayList<RecentFeeds>();
    private RecyclerAdapterRecentFeeds recyclerAdapterRecentFeeds;

    private View view;

    private List<MentorsHome> mentorsHomeList = new ArrayList<MentorsHome>();
    private RecyclerAdapterMentorsHome recyclerAdapterMentorsHome;

    private List<RecentlyGotPlaced> recentlyGotPlacedList = new ArrayList<RecentlyGotPlaced>();
    private RecyclerAdapterRecentlyGotPlaced recyclerAdapterRecentlyGotPlaced;

    private List<Testimonials> testimonialsList = new ArrayList<Testimonials>();
    private RecyclerAdapterTestimonials recyclerAdapterTestimonials;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        //getting context
        context = getContext();

        settingRecyclerView(view);

        return view;
    }

    private void settingRecyclerView(View view) {

        //mapping RecyclerView
        RecyclerView my_recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view_feed);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterRecentFeeds = new RecyclerAdapterRecentFeeds(context, recentFeedsList);

        //setting fixed size
        my_recycler_view.setHasFixedSize(true);
        //setting horizontal layout
        my_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //setting RecyclerView adapter
        my_recycler_view.setAdapter(recyclerAdapterRecentFeeds);
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
        recyclerAdapterRecentlyGotPlaced = new RecyclerAdapterRecentlyGotPlaced(context, recentlyGotPlacedList);

        //setting fixed size
        recently_placed_view.setHasFixedSize(true);
        //setting horizontal layout
        recently_placed_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //setting RecyclerView adapter
        recently_placed_view.setAdapter(recyclerAdapterRecentlyGotPlaced);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestRecentlyGotPlaced();


        //mapping RecyclerView
        RecyclerView testimonials_view = (RecyclerView) view.findViewById(R.id.recycler_view_testimonials);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterTestimonials = new RecyclerAdapterTestimonials(context, testimonialsList);

        //setting fixed size
        testimonials_view.setHasFixedSize(true);
        //setting horizontal layout
        testimonials_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //setting RecyclerView adapter
        testimonials_view.setAdapter(recyclerAdapterTestimonials);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestTestimonials();


        Button postButton = (Button) view.findViewById(R.id.postButtonHome);
        postButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {



            }
        });

        Button interviewButton = (Button) view.findViewById(R.id.interviewButtonHome);
        interviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button eventButton = (Button) view.findViewById(R.id.eventsButtonHome);
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button questionsButton = (Button) view.findViewById(R.id.questionsButtonHome);
        questionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    private void makeJsonArrayRequestTestimonials() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/homeService/testimonials", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Testimonials testimonials = new Testimonials(obj.getString("feedback"), obj.getString("user_name"),obj.getString("college"), obj.getString("imgname"));
                        // adding movie to testimonialsList array
                        testimonialsList.add(testimonials);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterTestimonials.notifyDataSetChanged();
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
                        RecentlyGotPlaced recentlyGotPlaced = new RecentlyGotPlaced(obj.getString("imgname"), obj.getString("username"), obj.getString("feedback"));
                        // adding movie to recentlyGotPlacedList array
                        recentlyGotPlacedList.add(recentlyGotPlaced);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterRecentlyGotPlaced.notifyDataSetChanged();
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
                        RecentFeeds recentFeeds = new RecentFeeds(obj.getString("type"), obj.getString("question"), obj.getString("industryname"), obj.getString("companyname"), obj.getString("modified_by"), obj.getString("name"), obj.getString("imgname"));
                        // adding movie to recentFeedsList array
                        recentFeedsList.add(recentFeeds);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterRecentFeeds.notifyDataSetChanged();
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


}
