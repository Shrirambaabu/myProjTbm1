package igotplaced.com.layouts.Fragments;


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

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterInterviewDetails;
import igotplaced.com.layouts.Model.Interview;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;


public class InterviewDetails extends Fragment implements View.OnClickListener {
    private String id = null, name = null, time = null, interview = null, image = null, industry = null, interviewUserId = null;

    private NetworkImageView interviewImage;
    private TextView profileName, profileTime, interviewMessage, interviewIndustry;

    private EditText userComment;
    private ImageView sendComment;


    private RecyclerAdapterInterviewDetails recyclerAdapterInterviewDetails;
    private RequestQueue queue;
    private List<Interview> interviewList = new ArrayList<Interview>();

    private String userId = null, userName = null;
    private String URL = BaseUri + "/home/interviewComments";
    private String userPostedComment;


    public InterviewDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);


        View view = inflater.inflate(R.layout.fragment_interview_details, container, false);

        addressingView(view);
        addingListeners(view);



        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("iid");
            name = bundle.getString("created_uname");
            time = bundle.getString("created_by");
            interview = bundle.getString("interview");
            image = bundle.getString("interviewImage");
            industry = bundle.getString("interviewIndustry");
            interviewUserId = bundle.getString("interview_createdid");
            Log.e("iid", id);
            Log.e("postImage", image);
        }


        interviewImage.setImageUrl(Utils.BaseImageUri + image, NetworkController.getInstance(getContext()).getImageLoader());
        profileName.setText(name);
        profileTime.setText(time);
        interviewMessage.setText(interview);
        interviewIndustry.setText(industry);


        postRecyclerView(view);


        // Inflate the layout for this fragment
        return view;
    }

    private void postRecyclerView(View view) {

        RecyclerView postRecycler = (RecyclerView) view.findViewById(R.id.comments_interview_recycler);
        recyclerAdapterInterviewDetails = new RecyclerAdapterInterviewDetails(getContext(), interviewList);
        //setting fixed size
        postRecycler.setHasFixedSize(true);
        //setting horizontal layout

        postRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //setting RecyclerView adapter
        postRecycler.setAdapter(recyclerAdapterInterviewDetails);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getContext()).getRequestQueue();

        makePostCommentsRequest();
    }

    private void makePostCommentsRequest() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/home/interviewCommentList/" + id, null,  new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                interviewList.clear();
                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {


                        JSONObject obj = response.getJSONObject(i);
                        Interview interview = new Interview(obj.getString("commentedUserImage"), obj.getString("comments"));
                        // adding movie to blogHomeList array
                        interviewList.add(interview);

                        Log.e("Comments",""+ obj.getString("comments"));

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterInterviewDetails.notifyDataSetChanged();
                    }
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

    private void addingListeners(View view) {
        sendComment.setOnClickListener(this);
    }

    private void addressingView(View view) {



        interviewImage = (NetworkImageView) view.findViewById(R.id.interview_img);
        profileName = (TextView) view.findViewById(R.id.interview_profile_name);
        profileTime = (TextView) view.findViewById(R.id.interview_time);
        interviewMessage = (TextView) view.findViewById(R.id.interview);
        interviewIndustry = (TextView) view.findViewById(R.id.interview_industry);
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
            recyclerAdapterInterviewDetails.notifyDataSetChanged();

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
                parameters.put("iid", id);
                parameters.put("intex_createrid", interviewUserId);
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

    /*class RecyclerAdapterInterviewDetails extends RecyclerView.Adapter<RecyclerAdapterInterviewDetails.MyViewHolder>{


         private List<Interview> interviewList;
         private Context context;
         private LayoutInflater inflater;


         public RecyclerAdapterInterviewDetails(Context context, List<Interview> interviewList) {
             this.context = context;
             this.interviewList = interviewList;
             inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         }

         @Override
         public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
             View rootView = inflater.inflate(R.layout.post_comments, parent, false);
             return new MyViewHolder(rootView);
         }

         @Override
         public void onBindViewHolder(MyViewHolder holder, int position) {
             Interview interview = interviewList.get(position);
             holder.commentedPost.setText(interview.getCommentMessage());
             holder.postImage.setImageUrl(Utils.BaseImageUri + interview.getComentUserImage(), NetworkController.getInstance(context).getImageLoader());

         }

         @Override
         public int getItemCount() {
             return interviewList.size();
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
     }*/
}
