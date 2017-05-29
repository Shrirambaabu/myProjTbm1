package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterPostHome;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class HomePostFragment extends Fragment {

    private Context context;
    private RequestQueue queue;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String userName, userId;
    private List<Post> postList = new ArrayList<Post>();
    private RecyclerAdapterPostHome recyclerAdapterPostHome;

    private static int current_page = 1;
    private int ival = 1;
    private int loadLimit = 10;

    public HomePostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        context = getActivity().getApplicationContext();

        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);

        postRecyclerView(view);

        return view;

    }

    private void postRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView post_view = (RecyclerView) view.findViewById(R.id.recycler_view_post);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterPostHome = new RecyclerAdapterPostHome(context, postList);

        //setting fixed size
        post_view.setHasFixedSize(true);
        //setting horizontal layout
        post_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //setting RecyclerView adapter
        post_view.setAdapter(recyclerAdapterPostHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();


        loadData(current_page);
        //Volley's inbuilt class to make Json array request

/*

        recyclerAdapterPostHome.setClickListener(this);
*/

    }


    private void makeJsonArrayRequestPostHome(int start,int size) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/home/topPost/" + userId + "?start=" + start + "&size=" + size, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                //clearing blogList
                postList.clear();

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Post post = new Post(obj.getString("post"), obj.getString("Industry"), obj.getString("imgname"), userName, obj.getString("created_by"));
                        // adding movie to blogHomeList array
                        postList.add(post);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterPostHome.notifyDataSetChanged();
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
    private void loadData(int current_page) {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        makeJsonArrayRequestPostHome(ival,loadLimit);


    }
    // adding 10 object creating dymically to arraylist and updating recyclerview when ever we reached last item
    private void loadMoreData(int current_page) {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request

        loadLimit = ival + 10;

        makeJsonArrayRequestPostHome(ival,loadLimit);

        recyclerAdapterPostHome.notifyDataSetChanged();

    }


}