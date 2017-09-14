package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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

public class ProfilePostFragment extends Fragment implements ClickListener {

    private Context context;
    private RequestQueue queue;


    private String userId;
    private List<Post> postList = new ArrayList<Post>();
    private RecyclerAdapterProfilePost recyclerAdapterProfilePost;
    private LinearLayoutManager mLayoutManager;


    public ProfilePostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_post, container, false);
        context = getActivity().getApplicationContext();

        mLayoutManager = new LinearLayoutManager(context);

        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        userId = sharedpreferences.getString(Id, null);

        postRecyclerView(view);

        return view;

    }


    private void postRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView post_view = (RecyclerView) view.findViewById(R.id.recycler_view_profile_post);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterProfilePost = new RecyclerAdapterProfilePost(context, postList);

        //setting fixed size
        post_view.setHasFixedSize(true);
        //setting horizontal layout
        post_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) post_view.getLayoutManager();
        //setting RecyclerView adapter
        post_view.setAdapter(recyclerAdapterProfilePost);

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();
        loadData();

        //    recyclerAdapterProfilePost.setClickListener(this);

    }


    private void makeJsonArrayRequestPostHome() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/profileService/profilePost/" + userId, null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject obj = response.getJSONObject(i);

                        Post post = new  Post(obj.getString("pid"),obj.getString("created_user"),obj.getString("post"), obj.getString("Industry"), obj.getString("post_created_user_image"), obj.getString("created_uname"), obj.getString("created_by"), obj.getString("post_created_user_image"), obj.getString("created_uname"),obj.getString("companyname"),obj.getString("company_id"));

                        postList.add(post);


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterProfilePost.notifyDataSetChanged();

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

    private void loadData() {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        makeJsonArrayRequestPostHome();
        // recyclerAdapterProfilePost.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view, int position) {
        /*BlogHome blog = blogHomeList.get(position);
        Intent i = new Intent(getContext(), BlogDetailsActivity.class);
        i.putExtra("postId", blog.getId());
        startActivity(i);*/
    }


    class RecyclerAdapterProfilePost extends RecyclerView.Adapter<RecyclerAdapterProfilePost.MyViewHolder> {

        private List<Post> postList;
        private Context context;
        private LayoutInflater inflater;


        public RecyclerAdapterProfilePost(Context context, List<Post> postList) {

            this.context = context;
            this.postList = postList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public RecyclerAdapterProfilePost.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_post, parent, false);
            return new RecyclerAdapterProfilePost.MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(RecyclerAdapterProfilePost.MyViewHolder holder, final int position) {

            Post post = postList.get(position);
            holder.post.setText(post.getPost());
            holder.postIndustry.setText("#"+post.getPostIndustry());
            if (post.getPostCompany().equals("")){
                holder.postCompany.setText(post.getPostCompany());
            }else{
                holder.postCompany.setText("#"+post.getPostCompany());
            }
            holder.postProfileName.setText(post.getPostProfileName());
            holder.postTime.setText(post.getPostTime());
            //  holder.userImage.setImageUrl(Utils.BaseImageUri + post.getUserImage(), NetworkController.getInstance(context).getImageLoader());
            holder.postImage.setImageUrl(Utils.BaseImageUri + post.getPostImage(), NetworkController.getInstance(context).getImageLoader());

            holder.postCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent companyDetails = new Intent(context, CompanyDetailsActivity.class);
                    companyDetails.putExtra("postCompany", postList.get(position).getPostCompany());
                    companyDetails.putExtra("companyId",  postList.get(position).getCompanyId());
                    startActivity(companyDetails);
                }
            });




            holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("tag", "click" + postList.get(position).getPostId());

                    Intent profileDetails=new Intent(getContext(), ProfilePostDetailsActivity.class);

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

        public class MyViewHolder extends RecyclerView.ViewHolder  {

            private TextView post, postIndustry, postProfileName, postTime,postCompany,viewMore;
            private NetworkImageView postImage;
            private ItemClickListener itemClickListener;

            public MyViewHolder(View itemView) {

                super(itemView);

                post = (TextView) itemView.findViewById(R.id.post);
                postIndustry = (TextView) itemView.findViewById(R.id.post_industry);
                postCompany = (TextView) itemView.findViewById(R.id.post_company);
                postProfileName = (TextView) itemView.findViewById(R.id.post_profile_name);
                postTime = (TextView) itemView.findViewById(R.id.post_time);
                // Volley's NetworkImageView which will load Image from URL
                postImage = (NetworkImageView) itemView.findViewById(R.id.post_img);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);

            }


        }
    }


}