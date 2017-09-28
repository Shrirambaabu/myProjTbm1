package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterNotification;
import igotplaced.com.layouts.Model.NotificationView;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ClickListener {

    private Context context;
    private RequestQueue queue;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String userId;
    private LinearLayoutManager mLayoutManager;
    private boolean loading;

    private List<NotificationView> notificationViewList = new ArrayList<NotificationView>();
    private RecyclerAdapterNotification recyclerAdapterNotification;


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        context = getActivity().getApplicationContext();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("Notifications");
        //mapping web view
        mapping(view);

        mLayoutManager = new LinearLayoutManager(context);

        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        userId = sharedpreferences.getString(Id, null);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        notificationRecyclerView(view);

        return view;
    }

    private void mapping(View view) {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onRefresh() {

        notificationViewList.clear();
        loadData();

    }

    private void loadData() {
        int loadLimit = 10;

        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestNotification(0, loadLimit);
    }

    private void notificationRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView notification_view = (RecyclerView) view.findViewById(R.id.card_recycler_view_notification);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterNotification = new RecyclerAdapterNotification(context, notificationViewList);

        //setting fixed size
        notification_view.setHasFixedSize(true);
        //setting horizontal layout
        notification_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) notification_view.getLayoutManager();
        //setting RecyclerView adapter
        notification_view.setAdapter(recyclerAdapterNotification);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();


        // show loader and fetch messages
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Volley's inbuilt class to make Json array request
                                        loadData();
                                    }
                                }
        );
        notification_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int totalItemCount;


                    totalItemCount = mLayoutManager.getItemCount();


                    if (!loading) {
                        Log.e("Tag", "Scroll");
                        loadMoreData(totalItemCount + 1);
                        loading = true;
                    }

                }
            }
        });


    }

    private void loadMoreData(int totalItemCount) {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        makeJsonArrayRequestNotification(totalItemCount, totalItemCount + 8);

        // recyclerAdapterNotification.notifyDataSetChanged();

    }

    private void makeJsonArrayRequestNotification(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUri + "/notificationService/notification/" + userId + "?start=" + start + "&size=" + size, null, new Response.Listener<JSONObject>() {
            JSONArray jsonObjectJSON = null;

            @Override
            public void onResponse(JSONObject jsonObject) {
               // notificationViewList.clear();
                try {
                    jsonObjectJSON = jsonObject.getJSONArray("");
                    //  notificationViewList.clear();

                    for (int i = 0; i < jsonObjectJSON.length(); i++) {
                     /*   Log.d("error", jsonObjectJSON.toString());*/
                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            NotificationView notify = new NotificationView(obj.getString("created_by"), obj.getString("post"), obj.getString("imgname"), obj.getString("ids"), obj.getString("Caption"));

                            // adding movie to blogHomeList array
                            notificationViewList.add(notify);

                        } catch (Exception e) {
                            Log.d("error", e.getMessage());
                            System.out.println(e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            recyclerAdapterNotification.notifyDataSetChanged();
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


    @Override
    public void onClick(View view, int position) {

      /*  NotificationView notify = notificationViewList.get(position);
        Intent i = new Intent(getContext(), BlogDetailsActivity.class);
        i.putExtra("postId", notify.getId());
        startActivity(i);*/
    }
}