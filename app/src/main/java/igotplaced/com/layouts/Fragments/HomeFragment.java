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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterRecentFeeds;
import igotplaced.com.layouts.Model.RecentFeeds;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;


public class HomeFragment extends Fragment {


    private Context context;
    private RequestQueue queue;
    private List<RecentFeeds> recentFeedsList = new ArrayList<RecentFeeds>();
    private RecyclerAdapterRecentFeeds recyclerAdapterRecentFeeds;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //getting context
        context = getContext();
        //mapping RecyclerView
        RecyclerView my_recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view_feed);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterRecentFeeds  = new RecyclerAdapterRecentFeeds(context,recentFeedsList);

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


        return view;
    }

    private void makeJsonArrayRequestRecentFeeds() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/homeService/recentFeeds", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
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
