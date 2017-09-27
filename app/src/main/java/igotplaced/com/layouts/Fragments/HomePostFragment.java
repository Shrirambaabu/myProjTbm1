package igotplaced.com.layouts.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import igotplaced.com.layouts.CompanyDetailsActivity;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.OtherProfileActivity;
import igotplaced.com.layouts.ProfilePostDetailsActivity;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

public class HomePostFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private RequestQueue queue;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String userId;
    private List<Post> postList = new ArrayList<Post>();
    private RecyclerAdapterPostHome recyclerAdapterPostHome;

    int loadLimit;
    private LinearLayoutManager mLayoutManager;
    private boolean loading;

    public HomePostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        context = getActivity().getApplicationContext();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("Posts");

        //mapping web view
        mapping(view);


        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        userId = sharedpreferences.getString(Id, null);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        postRecyclerView(view);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        if (screenSize(getActivity()) < 6.5) {
            loadLimit = 5;
            postList.clear();
            loadData();
        } else {

            loadLimit = 15;
            postList.clear();
            loadData();
        }

    }

    private void mapping(View view) {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }


    @Override
    public void onRefresh() {
        postList.clear();
        loadData();
    }

    private void postRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView post_view = (RecyclerView) view.findViewById(R.id.recycler_view_post);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterPostHome = new RecyclerAdapterPostHome(context, postList);
        //setting fixed size
        if (screenSize(getActivity()) < 6.5)
            mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        else {
            mLayoutManager = new GridLayoutManager(context, 2);
        }
        post_view.setHasFixedSize(true);
        //setting horizontal layout
        post_view.setLayoutManager(mLayoutManager);

        post_view.setAdapter(recyclerAdapterPostHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        //  loadData();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                Log.e("ScreenRUn", "SwipeRefresh");
                loadData();

            }
        });

        post_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int  totalItemCount;

                    totalItemCount = mLayoutManager.getItemCount();


                    if (!loading) {
                        loadMoreData(totalItemCount + 1);
                        loading = true;
                    }

                }
            }
        });


    }


    private void makeJsonObjectRequestPostHome(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }


        Log.d("error", "loaded" + BaseUri + "/home/topPost/" + userId + "?start=" + start + "&size=" + size);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUri + "/home/topPost/" + userId + "?start=" + start + "&size=" + size, null, new Response.Listener<JSONObject>() {
            JSONArray jsonObjectJSON = null;

            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    jsonObjectJSON = jsonObject.getJSONArray("");

                    //clearing blogList
                    //postList.clear();

                    for (int i = 0; i < jsonObjectJSON.length(); i++) {

                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            Post post = new Post(obj.getString("pid"), obj.getString("created_user"), obj.getString("post"), obj.getString("Industry"), obj.getString("postuserimgname"), obj.getString("created_uname"), obj.getString("created_by"), obj.getString("imgname"), obj.getString("fname"), obj.getString("companyname"), obj.getString("company_id"));
                            // adding movie to blogHomeList array
                            postList.add(post);


                        } catch (Exception e) {
                            Log.d("error", e.getMessage());
                            System.out.println(e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            recyclerAdapterPostHome.notifyDataSetChanged();
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
                Utils.showDialogue(getActivity(), "Sorry! Server Error");
            }
        });

        queue.add(jsonObjectRequest);

    }

    // By default, we add 10 objects for first time.
    private void loadData() {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request

        Log.e("ScreenRUn", "Load!1");

        makeJsonObjectRequestPostHome(0, loadLimit);

    }

    // adding 10 object creating dymically to arraylist and updating recyclerview when ever we reached last item
    private void loadMoreData(int totalItemCount) {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        makeJsonObjectRequestPostHome(totalItemCount, totalItemCount + 5);

        recyclerAdapterPostHome.notifyDataSetChanged();

    }


    class RecyclerAdapterPostHome extends RecyclerView.Adapter<RecyclerAdapterPostHome.MyViewHolder> {

        private String userId = null;


        private List<Post> postList;
        private Context context;
        private LayoutInflater inflater;


        public RecyclerAdapterPostHome(Context context, List<Post> postList) {
            SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            userId = sharedpreferences.getString(Id, null);
            this.context = context;
            this.postList = postList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_post, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            Post post = postList.get(position);

            String postedUserId;
            postedUserId = post.getPostedUserId();
            holder.post.setText(post.getPost());
            holder.postIndustry.setText("#" + post.getPostIndustry());
            holder.postProfileName.setText(post.getPostProfileName());
            holder.postTime.setText(post.getPostTime());
            if (post.getPostCompany().equals("")) {
                holder.postCompany.setText(post.getPostCompany());
            } else {
                holder.postCompany.setText("#" + post.getPostCompany());
            }

            holder.postImage.setImageUrl(Utils.BaseImageUri + post.getPostImage(), NetworkController.getInstance(context).getImageLoader());

            holder.postCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent companyDetails = new Intent(context, CompanyDetailsActivity.class);
                    companyDetails.putExtra("postCompany", postList.get(position).getPostCompany());
                    companyDetails.putExtra("companyId", postList.get(position).getCompanyId());
                    startActivity(companyDetails);
                }
            });
            if (!userId.equals(postedUserId)) {
                holder.postProfileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent otherProfileDetails = new Intent(context, OtherProfileActivity.class);

                        otherProfileDetails.putExtra("post_createdid", postList.get(position).getPostedUserId());
                        otherProfileDetails.putExtra("created_uname", postList.get(position).getPostProfileName());
                        startActivity(otherProfileDetails);
                    }
                });

            }
            holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("tag", "click" + postList.get(position).getPostId());

                    Intent profileDetails = new Intent(getContext(), ProfilePostDetailsActivity.class);

                    profileDetails.putExtra("pid", postList.get(position).getPostId());
                    profileDetails.putExtra("created_uname", postList.get(position).getPostProfileName());
                    profileDetails.putExtra("created_by", postList.get(position).getPostTime());
                    profileDetails.putExtra("post", postList.get(position).getPost());
                    profileDetails.putExtra("postImage", postList.get(position).getPostImage());
                    profileDetails.putExtra("postIndustry", postList.get(position).getPostIndustry());
                    profileDetails.putExtra("post_createdid", postList.get(position).getPostedUserId());
                    profileDetails.putExtra("postCompany", postList.get(position).getPostCompany());
                    profileDetails.putExtra("companyId", postList.get(position).getCompanyId());

                    startActivity(profileDetails);
                }
            });


        }


        @Override
        public int getItemCount() {
            return postList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView post, postIndustry, postProfileName, postTime, postCompany, viewMore;

            private NetworkImageView postImage;


            public MyViewHolder(View itemView) {
                super(itemView);
                post = (TextView) itemView.findViewById(R.id.post);
                postIndustry = (TextView) itemView.findViewById(R.id.post_industry);
                postProfileName = (TextView) itemView.findViewById(R.id.post_profile_name);
                postTime = (TextView) itemView.findViewById(R.id.post_time);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);
                postCompany = (TextView) itemView.findViewById(R.id.post_company);

                postImage = (NetworkImageView) itemView.findViewById(R.id.post_img);
            }
        }

    }


}