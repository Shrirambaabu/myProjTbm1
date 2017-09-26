package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
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
import igotplaced.com.layouts.Model.Questions;
import igotplaced.com.layouts.OtherProfileActivity;
import igotplaced.com.layouts.ProfileQuestionsDetailsActivity;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.screenSize;


public class OtherProfileQuestionsFragment extends Fragment {

    private String userId;
    private   TextView noData;
    private Context context;
    private RequestQueue queue;
    private List<Questions> questionsList = new ArrayList<Questions>();
    private RecyclerAdapterOtherProfileQuestions recyclerAdapterOtherProfileQuestions;
    private LinearLayoutManager mLayoutManager;

    public OtherProfileQuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_profile_questions, container, false);
        context = getActivity().getApplicationContext();

       // mLayoutManager = new LinearLayoutManager(context);
        noData=(TextView) view.findViewById(R.id.no_data);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userId = bundle.getString("otherId");
        }

        postRecyclerView(view);
        return view;
    }

    private void postRecyclerView(View view) {

        //mapping RecyclerView
        RecyclerView post_view = (RecyclerView) view.findViewById(R.id.recycler_view_profile_questions);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterOtherProfileQuestions = new RecyclerAdapterOtherProfileQuestions(context, questionsList);


        //setting fixed size
        Log.e("ScreenSizeReecyvlr", "" + screenSize(getActivity()));
        if (screenSize(getActivity()) < 6.5)
            mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        else {
            mLayoutManager = new GridLayoutManager(context, 2);
        }
        post_view.setHasFixedSize(true);
        //setting horizontal layout
        post_view.setLayoutManager(mLayoutManager);
        post_view.setAdapter(recyclerAdapterOtherProfileQuestions);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();
        makeJsonArrayRequestQuestionsHome();

    }

    private void makeJsonArrayRequestQuestionsHome() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/profileService/profileQuestion/" + userId, null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                questionsList.clear();
                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        Questions questions = new Questions(obj.getString("qid"), obj.getString("created_user"), obj.getString("question"), obj.getString("industryname"), obj.getString("questionimgname"), obj.getString("created_uname"), obj.getString("created_by"), obj.getString("questionimgname"), obj.getString("created_uname"),obj.getString("companyname"),obj.getString("company_id"));
                        // adding movie to blogHomeList array
                        questionsList.add(questions);


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterOtherProfileQuestions.notifyDataSetChanged();
                    }
                }
                if (questionsList.isEmpty()){
                    noData.setVisibility(View.VISIBLE);
                }else {
                    noData.setVisibility(View.GONE);
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

    private class RecyclerAdapterOtherProfileQuestions extends RecyclerView.Adapter<RecyclerAdapterOtherProfileQuestions.MyViewHolder> {

        private List<Questions> questionsList;
        private Context context;
        private LayoutInflater inflater;

        public RecyclerAdapterOtherProfileQuestions(Context context, List<Questions> questionsList) {


            this.context = context;
            this.questionsList = questionsList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public RecyclerAdapterOtherProfileQuestions.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_questions, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(RecyclerAdapterOtherProfileQuestions.MyViewHolder holder, final int position) {

            Questions questions = questionsList.get(position);

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

            holder.questionsCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent companyDetails=new Intent(context,CompanyDetailsActivity.class);
                    companyDetails.putExtra("postCompany", questionsList.get(position).getQuestionsCompany());
                    companyDetails.putExtra("companyId", questionsList.get(position).getCompanyId());
                    startActivity(companyDetails);
                }
            });
            if (!questionsList.get(position).getQuestionUserId().equals(userId)) {

                holder.questionsProfileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent otherProfileDetails = new Intent(context, OtherProfileActivity.class);

                        otherProfileDetails.putExtra("post_createdid", questionsList.get(position).getQuestionUserId());
                        otherProfileDetails.putExtra("created_uname", questionsList.get(position).getQuestionsProfileName());
                        startActivity(otherProfileDetails);
                    }
                });
            }
            holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView questions, questionsIndustry, questionsProfileName, questionsTime,questionsCompany, viewMore;
            private NetworkImageView questionsImage;


            public MyViewHolder(View itemView) {
                super(itemView);

                questions = (TextView) itemView.findViewById(R.id.questions);
                questionsIndustry = (TextView) itemView.findViewById(R.id.questions_industry);
                questionsProfileName = (TextView) itemView.findViewById(R.id.questions_profile_name);
                questionsTime = (TextView) itemView.findViewById(R.id.questions_time);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);
                questionsCompany = (TextView) itemView.findViewById(R.id.questions_company);

                // Volley's NetworkImageView which will load Image from URL
                questionsImage = (NetworkImageView) itemView.findViewById(R.id.questions_img);


            }



        }
    }
}
