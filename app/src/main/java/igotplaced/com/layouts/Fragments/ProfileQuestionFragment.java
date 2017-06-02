package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterQuestionsHome;
import igotplaced.com.layouts.Model.Events;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.Model.Questions;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class ProfileQuestionFragment extends Fragment implements ClickListener {

    private Context context;
    private RequestQueue queue;


    private String userId;
    private List<Questions> questionsList = new ArrayList<Questions>();
    private RecyclerAdapterQuestionsHome recyclerAdapterQuestionsHome;

    int lastVisiblesItems, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLayoutManager;
    private boolean loading, swipe = false;

    public ProfileQuestionFragment() {
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
        String userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);

        postRecyclerView(view);

        return view;

    }




    private void postRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView post_view = (RecyclerView) view.findViewById(R.id.recycler_view_profile_post);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterQuestionsHome = new RecyclerAdapterQuestionsHome(context, questionsList);

        //setting fixed size
        post_view.setHasFixedSize(true);
        //setting horizontal layout
        post_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) post_view.getLayoutManager();
        //setting RecyclerView adapter
        post_view.setAdapter(recyclerAdapterQuestionsHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        loadData();



        recyclerAdapterQuestionsHome.setClickListener(this);

    }


    private void makeJsonArrayRequestQuestionsHome() {



        Log.d("error", "loaded" + BaseUri + "/profileService/profileQuestion/"+userId );

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/profileService/profileQuestion/"+ userId, null,  new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        questionsList.clear();
                        JSONObject obj = response.getJSONObject(i);
                        Questions questions = new Questions(obj.getString("question"), obj.getString("industryname"), obj.getString("questionimgname"), obj.getString("created_uname"), obj.getString("created_by"), obj.getString("questionimgname"), obj.getString("created_uname"));
                        // adding movie to blogHomeList array
                        questionsList.add(questions);



                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterQuestionsHome.notifyDataSetChanged();
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

    // By default, we add 10 objects for first time.
    private void loadData() {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request

        makeJsonArrayRequestQuestionsHome();


        recyclerAdapterQuestionsHome.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view, int position) {
        /*BlogHome blog = blogHomeList.get(position);
        Intent i = new Intent(getContext(), BlogDetailsActivity.class);
        i.putExtra("postId", blog.getId());
        startActivity(i);*/
    }
}