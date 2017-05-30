package igotplaced.com.layouts.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterInterviewHome;
import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterPostHome;
import igotplaced.com.layouts.Model.Interview;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class HomeInterviewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ClickListener {

    private Context context;
    private RequestQueue queue;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String userId;
    private List<Interview> interviewList = new ArrayList<Interview>();
    private RecyclerAdapterInterviewHome recyclerAdapterInterviewHome;

    int lastVisiblesItems, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLayoutManager;
    private boolean loading, swipe = false;

    public HomeInterviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interview, container, false);
        context = getActivity().getApplicationContext();


        //mapping web view
        mapping(view);

        mLayoutManager = new LinearLayoutManager(context);

        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        interviewRecyclerView(view);

        return view;

    }

    private void mapping(View view) {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }


    @Override
    public void onRefresh() {
        interviewList.clear();
        loadData();
    }

    private void interviewRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView interview_view = (RecyclerView) view.findViewById(R.id.recycler_view_interview);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterInterviewHome = new RecyclerAdapterInterviewHome(context, interviewList);

        //setting fixed size
        interview_view.setHasFixedSize(true);
        //setting horizontal layout
        interview_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) interview_view.getLayoutManager();
        //setting RecyclerView adapter
        interview_view.setAdapter(recyclerAdapterInterviewHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        //  loadData();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadData();

            }
        });

        interview_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {

                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    lastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
/*

                    Log.d("error", ""+visibleItemCount+totalItemCount+lastVisiblesItems);
*/

                    if (!loading) {
                        loadMoreData(totalItemCount + 1);
                        loading = true;
                    }

                }
            }
        });

        recyclerAdapterInterviewHome.setClickListener(this);

    }


    private void makeJsonObjectRequestInterviewHome(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }


        Log.d("error", "loaded" + BaseUri + "/home/topPost/" + userId + "?start=" + start + "&size=" + size);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUri + "/home/topInterviewExperience/" + userId + "?start=" + start + "&size=" + size, null, new Response.Listener<JSONObject>() {
            JSONArray jsonObjectJSON = null;

            @Override
            public void onResponse(JSONObject jsonObject) {
/*
                Log.d("error", jsonObject.toString());*/
                try {
                    jsonObjectJSON = jsonObject.getJSONArray("");

                    //clearing blogList
                    // postList.clear();

                    for (int i = 0; i < jsonObjectJSON.length(); i++) {
                     /*   Log.d("error", jsonObjectJSON.toString());*/
                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            Interview interview = new Interview(obj.getString("feedback"), obj.getString("industryname"), obj.getString("interviewUserImgName"), obj.getString("username"), obj.getString("created_by"), obj.getString("imgname"), obj.getString("fname"));
                            // adding movie to blogHomeList array
                            interviewList.add(interview);

                        } catch (Exception e) {
                            Log.d("error", e.getMessage());
                            System.out.println(e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            recyclerAdapterInterviewHome.notifyDataSetChanged();
                            loading = false;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());

            }
        });

        queue.add(jsonObjectRequest);

    }

    // By default, we add 10 objects for first time.
    private void loadData() {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        int loadLimit = 10;
        makeJsonObjectRequestInterviewHome(1, loadLimit);

    }

    // adding 10 object creating dymically to arraylist and updating recyclerview when ever we reached last item
    private void loadMoreData(int totalItemCount) {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        makeJsonObjectRequestInterviewHome(totalItemCount, totalItemCount + 10);

        recyclerAdapterInterviewHome.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view, int position) {
        /*BlogHome blog = blogHomeList.get(position);
        Intent i = new Intent(getContext(), BlogDetailsActivity.class);
        i.putExtra("postId", blog.getId());
        startActivity(i);*/
    }
}