package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterBlogHome;
import igotplaced.com.layouts.Model.BlogHome;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.BlogDetailsActivity;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class BlogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ClickListener {

    private Context context;
    private RequestQueue queue;

    private SwipeRefreshLayout swipeRefreshLayout;
    private String userId;
    int lastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    private boolean loading, swipe = false;
    private List<BlogHome> blogHomeList = new ArrayList<BlogHome>();
    private RecyclerAdapterBlogHome recyclerAdapterBlogHome;


    public BlogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        context = getActivity().getApplicationContext();


        //mapping web view
        mapping(view);

        mLayoutManager = new LinearLayoutManager(context);

        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        blogRecyclerView(view);

        return view;
    }

    private void mapping(View view) {


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {


        blogHomeList.clear();
        loadData();

    }

    private void loadData() {
        int loadLimit = 4;

        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestBlog(0,loadLimit);

    }

    private void blogRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView blog_view = (RecyclerView) view.findViewById(R.id.recycler_view_blog);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterBlogHome = new RecyclerAdapterBlogHome(context, blogHomeList);

        //setting fixed size
        blog_view.setHasFixedSize(true);
        //setting horizontal layout
        blog_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //setting RecyclerView adapter
        blog_view.setAdapter(recyclerAdapterBlogHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        //Volley's inbuilt class to make Json array request
                        loadData();
                    }
                }
        );

        blog_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        loadMoreData(totalItemCount);
                        loading = true;
                    }

                }
            }
        });

        recyclerAdapterBlogHome.setClickListener(this);

    }

    private void loadMoreData(int totalItemCount) {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        makeJsonArrayRequestBlog(totalItemCount, totalItemCount + 3 );

        recyclerAdapterBlogHome.notifyDataSetChanged();


    }

    private void makeJsonArrayRequestBlog(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }




        Log.d("error", "loaded" + BaseUri + "/blogService/blog" + userId + "?start=" + start + "&size=" + size);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUri + "/blogService/blog?start=" + start + "&size=" + size, null, new Response.Listener<JSONObject>() {
            JSONArray jsonObjectJSON = null;

            @Override
            public void onResponse(JSONObject jsonObject) {
/*
                Log.d("error", jsonObject.toString());*/
                try {
                    jsonObjectJSON = jsonObject.getJSONArray("");

                    blogHomeList.clear();

                    for (int i = 0; i < jsonObjectJSON.length(); i++) {
                     /*   Log.d("error", jsonObjectJSON.toString());*/
                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            BlogHome blogHome = new BlogHome(obj.getString("image"), obj.getString("header"), obj.getString("author"), obj.getString("modified_by"), obj.getString("id"));
                            // adding movie to blogHomeList array
                            blogHomeList.add(blogHome);

                        } catch (Exception e) {
                            Log.d("error", e.getMessage());
                            System.out.println(e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            recyclerAdapterBlogHome.notifyDataSetChanged();
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

    @Override
    public void onClick(View view, int position) {
        BlogHome blog = blogHomeList.get(position);
        Intent i = new Intent(getContext(), BlogDetailsActivity.class);
        i.putExtra("postId", blog.getId());
        startActivity(i);
    }
}
