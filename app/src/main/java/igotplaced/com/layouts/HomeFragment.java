package igotplaced.com.layouts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.Model.RecentFeeds;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;


public class HomeFragment extends Fragment {


    private Context context;
    private RequestQueue queue;
    private List<RecentFeeds> recentFeedsList = new ArrayList<RecentFeeds>();
    private RecyclerAdapterRecentFeeds recyclerAdapterRecentFeeds;
    private RecyclerView my_recycler_view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        context = getContext();

        my_recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view_feed);

        my_recycler_view.setHasFixedSize(true);

        recyclerAdapterRecentFeeds = new RecyclerAdapterRecentFeeds(context, recentFeedsList);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(recyclerAdapterRecentFeeds);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        makeJsonObjectRequestRecentFeeds();


        return view;
    }

    private void makeJsonObjectRequestRecentFeeds() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, BaseUri + "/homeService/recentFeeds", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("error", response.toString());


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        queue.add(jsonObjReq);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
