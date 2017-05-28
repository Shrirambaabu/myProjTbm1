package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterBlogHome;
import igotplaced.com.layouts.Model.BlogHome;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.BlogDetailsActivity;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;

public class BlogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ClickListener {

    private Context context;
    private RequestQueue queue;

    private SwipeRefreshLayout swipeRefreshLayout;

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

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        blogRecyclerView(view);

        return view;
    }

    @Override
    public void onRefresh() {
        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestBlog();

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
                        makeJsonArrayRequestBlog();
                    }
                }
        );

        recyclerAdapterBlogHome.setClickListener(this);

    }

    private void makeJsonArrayRequestBlog() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/blogService/blog", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                //clearing blogList
                blogHomeList.clear();

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        BlogHome blogView = new BlogHome(obj.getString("image"), obj.getString("header"), obj.getString("author"), obj.getString("modified_by"), obj.getString("id"));
                        // adding movie to blogHomeList array
                        blogHomeList.add(blogView);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterBlogHome.notifyDataSetChanged();
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
    public void onClick(View view, int position) {
        BlogHome blog = blogHomeList.get(position);
        Intent i = new Intent(getContext(), BlogDetailsActivity.class);
        i.putExtra("postId", blog.getId());
        startActivity(i);
    }
}
