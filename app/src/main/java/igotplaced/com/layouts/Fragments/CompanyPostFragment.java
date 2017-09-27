package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.CompanyDetailsActivity;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.OtherProfileActivity;
import igotplaced.com.layouts.ProfilePostDetailsActivity;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Email;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

public class CompanyPostFragment extends Fragment {
    private   TextView noData;
    private   ImageView logo;
    private Context context;
    private RequestQueue queue;
    private String userId;
    private List<Post> postList = new ArrayList<Post>();
    private RecyclerCompanyPost recyclerCompanyPost;

    public CompanyPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_post, container, false);

        context = getActivity().getApplicationContext();
        Bundle bundle = this.getArguments();
        noData=(TextView) view.findViewById(R.id.no_data);
        logo=(ImageView) view.findViewById(R.id.logo);
        if (bundle != null) {
            userId = bundle.getString("otherId");
        }
        //   mLayoutManager = new LinearLayoutManager(context);
        postRecyclerView(view);
        return view;
    }

    private void postRecyclerView(View view) {

        //mapping RecyclerView
        RecyclerView post_view = (RecyclerView) view.findViewById(R.id.recycler_view_profile_post);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerCompanyPost = new RecyclerCompanyPost(context, postList);


        LinearLayoutManager mLayoutManager;
        if (screenSize(getActivity()) < 6.5)
            mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        else {
            mLayoutManager = new GridLayoutManager(context, 2);
        }
        post_view.setHasFixedSize(true);
        //setting horizontal layout
        post_view.setLayoutManager(mLayoutManager);
        post_view.setAdapter(recyclerCompanyPost);

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        makeJsonArrayRequestPostHome();
    }

    private void makeJsonArrayRequestPostHome() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/profileService/companyProfilePost/" + userId, null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {

                postList.clear();
                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject obj = response.getJSONObject(i);

                        Post post = new Post(obj.getString("pid"), obj.getString("created_user"), obj.getString("post"), obj.getString("industry"), obj.getString("companyPostImage"), obj.getString("created_uname"), obj.getString("created_by"), obj.getString("companyPostImage"), obj.getString("created_uname"), obj.getString("companyname"), obj.getString("company_id"));

                        postList.add(post);


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerCompanyPost.notifyDataSetChanged();

                    }
                }
                if (postList.isEmpty()){
                    noData.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.VISIBLE);
                }else {
                    noData.setVisibility(View.GONE);
                    logo.setVisibility(View.GONE);
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
                Utils.showDialogue(getActivity(),"Sorry! Server Error");
            }
        });

        queue.add(jsonArrayRequest);
    }

    private class RecyclerCompanyPost extends RecyclerView.Adapter<RecyclerCompanyPost.MyViewHolder> {

        private List<Post> postList;
        private Context context;
        private LayoutInflater inflater;
        private SharedPreferences sharedpreferences;
        private String id;

        public RecyclerCompanyPost(Context context, List<Post> postList) {

            this.context = context;
            this.postList = postList;
            sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            id = sharedpreferences.getString(Id, null);

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public RecyclerCompanyPost.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_post, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(RecyclerCompanyPost.MyViewHolder holder, final int position) {

            Post post = postList.get(position);
            holder.post.setText(post.getPost());
            holder.postIndustry.setText("#" + post.getPostIndustry());
            if (post.getPostCompany().equals("")) {
                holder.postCompany.setText(post.getPostCompany());
            } else {
                holder.postCompany.setText("#" + post.getPostCompany());
            }
            holder.postProfileName.setText(post.getPostProfileName());
            holder.postTime.setText(post.getPostTime());
            //  holder.userImage.setImageUrl(Utils.BaseImageUri + post.getUserImage(), NetworkController.getInstance(context).getImageLoader());
            holder.postImage.setImageUrl(Utils.BaseImageUri + post.getPostImage(), NetworkController.getInstance(context).getImageLoader());

            if (!id.equals(postList.get(position).getPostedUserId())) {
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
                postCompany = (TextView) itemView.findViewById(R.id.post_company);
                postProfileName = (TextView) itemView.findViewById(R.id.post_profile_name);
                postTime = (TextView) itemView.findViewById(R.id.post_time);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);
                // Volley's NetworkImageView which will load Image from URL
                postImage = (NetworkImageView) itemView.findViewById(R.id.post_img);


            }


        }
    }
}
