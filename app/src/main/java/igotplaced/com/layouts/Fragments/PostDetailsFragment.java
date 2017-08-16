package igotplaced.com.layouts.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import igotplaced.com.layouts.Model.Interview;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class PostDetailsFragment extends Fragment implements View.OnClickListener {

    private String id = null, name = null, time = null, post = null, image = null, industry = null, postUserId = null;

    private NetworkImageView postImage;
    private TextView profileName, profileTime, postMessage, postIndustry;

    private EditText userComment;
    private ImageView sendComment;

    private List<Post> postList = new ArrayList<Post>();
    private LinearLayoutManager mLayoutManager;

    private RequestQueue queue;


    private RecyclerAdapterPostDetails recyclerAdapterPostDetails;

    private String userId = null, userName = null;
    private String URL = BaseUri + "/home/postComments";
    private String userPostedComment;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        addressingView(view);
        addingListeners(view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("pid");
            name = bundle.getString("created_uname");
            time = bundle.getString("created_by");
            post = bundle.getString("post");
            image = bundle.getString("postImage");
            industry = bundle.getString("postIndustry");
            postUserId = bundle.getString("post_createdid");
            Log.e("pid", id);
            Log.e("created_uname", name);
            Log.e("created_by", time);
            Log.e("post", post);
            Log.e("postImage", image);
            Log.e("postIndustry", industry);
            Log.e("post_createdid", postUserId);
        }

        postImage.setImageUrl(Utils.BaseImageUri + image, NetworkController.getInstance(getContext()).getImageLoader());
        profileName.setText(name);
        profileTime.setText(time);
        postMessage.setText(post);
        postIndustry.setText(industry);

        postRecyclerView(view);

        return view;

    }

    private void postRecyclerView(View view) {

        RecyclerView postRecycler = (RecyclerView) view.findViewById(R.id.comments_post_recycler);
        recyclerAdapterPostDetails = new RecyclerAdapterPostDetails(getContext(), postList);
        //setting fixed size
        postRecycler.setHasFixedSize(true);
        //setting horizontal layout
        postRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) postRecycler.getLayoutManager();
        //setting RecyclerView adapter
        postRecycler.setAdapter(recyclerAdapterPostDetails);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getContext()).getRequestQueue();

        makePostCommentsRequest();


    }

    private void makePostCommentsRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/home/postCommentList/" + id, null,  new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                postList.clear();
                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {


                        JSONObject obj = response.getJSONObject(i);
                        Post post = new Post(obj.getString("commentedUserImage"), obj.getString("comments"));
                        // adding movie to blogHomeList array
                        postList.add(post);

                        Log.e("Comments",""+ obj.getString("comments"));

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterPostDetails.notifyDataSetChanged();
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

    private void addingListeners(View view) {
        sendComment.setOnClickListener(this);

    }

    private void addressingView(View view) {

        postImage = (NetworkImageView) view.findViewById(R.id.post_img);
        profileName = (TextView) view.findViewById(R.id.post_profile_name);
        profileTime = (TextView) view.findViewById(R.id.post_time);
        postMessage = (TextView) view.findViewById(R.id.post);
        postIndustry = (TextView) view.findViewById(R.id.post_industry);
        userComment = (EditText) view.findViewById(R.id.user_comment);
        sendComment = (ImageView) view.findViewById(R.id.send_comment);

    }

    @Override
    public void onClick(View v) {

        if (userComment.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter the Comment", Toast.LENGTH_SHORT).show();
        } else {
            userPostedComment = userComment.getText().toString();
            insertUserComment();
            recyclerAdapterPostDetails.notifyDataSetChanged();
            Toast.makeText(getContext(), "Comment added", Toast.LENGTH_SHORT).show();
        }
        userComment.setText("");
    }

    private void insertUserComment() {

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(getActivity(), "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("pid", id);
                parameters.put("post_createdid", postUserId);
                parameters.put("user_id", userId);
                parameters.put("comments", userPostedComment);
                parameters.put("created_uname", userName);
                Log.e("param", "" + parameters);
                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 30000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }


    class RecyclerAdapterPostDetails extends RecyclerView.Adapter<RecyclerAdapterPostDetails.MyViewHolder> {

        private List<Post> postList;
        private Context context;
        private LayoutInflater inflater;

        public RecyclerAdapterPostDetails(Context context, List<Post> postList) {

            this.context = context;
            this.postList = postList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.post_comments, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Post post = postList.get(position);
            holder.commentedPost.setText(post.getCommentedMessage());
            holder.postImage.setImageUrl(Utils.BaseImageUri + post.getComentedUserImage(), NetworkController.getInstance(context).getImageLoader());

        }

        @Override
        public int getItemCount() {
            return postList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private NetworkImageView postImage;
            private TextView commentedPost;

            public MyViewHolder(View itemView) {
                super(itemView);

                postImage=(NetworkImageView) itemView.findViewById(R.id.post_img);
                commentedPost=(TextView) itemView.findViewById(R.id.commented_post);
            }
        }
    }


}