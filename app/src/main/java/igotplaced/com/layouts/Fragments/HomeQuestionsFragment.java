package igotplaced.com.layouts.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import igotplaced.com.layouts.Model.Questions;
import igotplaced.com.layouts.ProfileQuestionsDetailsActivity;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;


public class HomeQuestionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ClickListener {

    private Context context;
    private RequestQueue queue;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String userId;
    private List<Questions> questionsList = new ArrayList<Questions>();
    private RecyclerAdapterQuestionsHome recyclerAdapterQuestionsHome;

    int lastVisiblesItems, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLayoutManager;
    private boolean loading, swipe = false;

    public HomeQuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_questions, container, false);
        context = getActivity().getApplicationContext();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("Questions");
        //mapping web view
        mapping(view);

        mLayoutManager = new LinearLayoutManager(context);

        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        questionRecyclerView(view);

        return view;

    }

    private void mapping(View view) {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }


    @Override
    public void onRefresh() {
        questionsList.clear();
        loadData();
    }

    private void questionRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView questions_view = (RecyclerView) view.findViewById(R.id.recycler_view_questions);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterQuestionsHome = new RecyclerAdapterQuestionsHome(context, questionsList);

        //setting fixed size
        questions_view.setHasFixedSize(true);
        //setting horizontal layout
        questions_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) questions_view.getLayoutManager();
        //setting RecyclerView adapter
        questions_view.setAdapter(recyclerAdapterQuestionsHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        //  loadData();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadData();

            }
        });

        questions_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

      //  recyclerAdapterQuestionsHome.setClickListener(this);

    }


    private void makeJsonObjectRequestQuestionsHome(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }


        Log.d("error", "loaded" + BaseUri + "/home/topQuestion/" + userId + "?start=" + start + "&size=" + size);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUri + "/home/topQuestion/" + userId + "?start=" + start + "&size=" + size, null, new Response.Listener<JSONObject>() {
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
                            Questions questions = new Questions(obj.getString("id"),obj.getString("created_user"),obj.getString("question"), obj.getString("industryname"), obj.getString("questionUserImgName"), obj.getString("created_uname"), obj.getString("created_by"), obj.getString("imgname"), obj.getString("fname"),obj.getString("companyname"),obj.getString("company_id"));
                            // adding movie to blogHomeList array
                            questionsList.add(questions);

                        } catch (Exception e) {
                            Log.d("error", e.getMessage());
                            System.out.println(e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            recyclerAdapterQuestionsHome.notifyDataSetChanged();
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
        int loadLimit = 5;
        makeJsonObjectRequestQuestionsHome(0, loadLimit);

    }

    // adding 10 object creating dymically to arraylist and updating recyclerview when ever we reached last item
    private void loadMoreData(int totalItemCount) {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        makeJsonObjectRequestQuestionsHome(totalItemCount, totalItemCount + 5);

        recyclerAdapterQuestionsHome.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view, int position) {
        /*BlogHome blog = blogHomeList.get(position);
        Intent i = new Intent(getContext(), BlogDetailsActivity.class);
        i.putExtra("postId", blog.getId());
        startActivity(i);*/
    }


    class RecyclerAdapterQuestionsHome extends RecyclerView.Adapter<RecyclerAdapterQuestionsHome.MyViewHolder>{

        private String userId = null, userName = null;
        private String URL = BaseUri + "/home/questionsComments";
        private String questionsId, postedQuestionUserId, userPostedComment;

        private List<Questions> questionsList;
        private Context context;
        private LayoutInflater inflater;

        public RecyclerAdapterQuestionsHome(Context context, List<Questions> questionsList) {
            SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            userName = sharedpreferences.getString(Name, null);
            userId = sharedpreferences.getString(Id, null);
            this.context = context;
            this.questionsList = questionsList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }




        @Override
        public RecyclerAdapterQuestionsHome.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_questions, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final RecyclerAdapterQuestionsHome.MyViewHolder holder, final int position) {
            Questions questions = questionsList.get(position);
            questionsId = questions.getQuestionId();
            postedQuestionUserId = questions.getQuestionUserId();
            //Pass the values of feeds object to Views
            holder.questions.setText(questions.getQuestions());
            if(questions.getQuestionsCompany().equals("")){
                holder.questionsCompany.setText(questions.getQuestionsCompany());
            }else {
                holder.questionsCompany.setText("#"+questions.getQuestionsCompany());
            }

            holder.questionsIndustry.setText("#"+questions.getQuestionsIndustry());
            holder.questionsProfileName.setText(questions.getQuestionsProfileName());
            holder.questionsTime.setText(questions.getQuestionsTime());
            //      holder.comment_profile_img.setImageUrl(Utils.BaseImageUri + questions.getCommentProfileImage(), NetworkController.getInstance(context).getImageLoader());
            holder.questionsImage.setImageUrl(Utils.BaseImageUri + questions.getQuestionsImage(), NetworkController.getInstance(context).getImageLoader());



            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    Intent questionDetails=new Intent(getContext(), ProfileQuestionsDetailsActivity.class);

                    questionDetails.putExtra("qid", questionsList.get(position).getQuestionId());
                    questionDetails.putExtra("created_uname", questionsList.get(position).getQuestionsProfileName());
                    questionDetails.putExtra("created_by", questionsList.get(position).getQuestionsTime());
                    questionDetails.putExtra("question", questionsList.get(position).getQuestions());
                    questionDetails.putExtra("postImage", questionsList.get(position).getQuestionsImage());
                    questionDetails.putExtra("postIndustry", questionsList.get(position).getQuestionsIndustry());
                    questionDetails.putExtra("post_createdid", questionsList.get(position).getQuestionUserId());
                    questionDetails.putExtra("postCompany", questionsList.get(position).getQuestionsCompany());
                    questionDetails.putExtra("companyId", questionsList.get(position).getCompanyId());
                    startActivity(questionDetails);
                }
            });
        }


        @Override
        public int getItemCount() {
            return questionsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView questions, questionsIndustry, questionsProfileName,questionsCompany, questionsTime,viewMore;
            private NetworkImageView questionsImage, comment_profile_img;

            private ItemClickListener itemClickListener;

            public MyViewHolder(View itemView) {
                super(itemView);
                questions = (TextView) itemView.findViewById(R.id.questions);
                questionsIndustry = (TextView) itemView.findViewById(R.id.questions_industry);
                questionsCompany = (TextView) itemView.findViewById(R.id.questions_company);
                questionsProfileName = (TextView) itemView.findViewById(R.id.questions_profile_name);
                questionsTime = (TextView) itemView.findViewById(R.id.questions_time);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);


                // Volley's NetworkImageView which will load Image from URL
                questionsImage = (NetworkImageView) itemView.findViewById(R.id.questions_img);
                //      comment_profile_img = (NetworkImageView) itemView.findViewById(R.id.comment_profile_img);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                this.itemClickListener.onItemClick(v, getLayoutPosition());
            }
            void setItemClickListener(ItemClickListener ic) {
                this.itemClickListener = ic;
            }
        }
    }




    }


