package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterSearchDetails;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.Model.SearchResultsModel;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;


public class SearchResults extends Fragment {


    private String Query;
    private Context context;
    private RequestQueue queue;
    private LinearLayoutManager mLayoutManager;
    ArrayList list= new ArrayList();
    private TextView noResult;
    private List<SearchResultsModel> searchResultsList = new ArrayList<>();


    private RecyclerAdapterSearchDetails recyclerAdapterSearchDetails;

    public SearchResults() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_results, container, false);
        // Inflate the layout for this fragment

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            Query = bundle.getString("QuerySubmit");
        }
        Log.e("QuerySubmit Result", Query);

        noResult=(TextView) view.findViewById(R.id.no_result);

        searchRecyclerView(view);

        return view;
    }

    private void searchRecyclerView(View view) {

        RecyclerView searchRecycler = (RecyclerView) view.findViewById(R.id.recycler_view_search_results);


        recyclerAdapterSearchDetails = new RecyclerAdapterSearchDetails(getContext(), searchResultsList);
        //setting fixed size
        searchRecycler.setHasFixedSize(true);
        //setting horizontal layout
        searchRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) searchRecycler.getLayoutManager();
        //setting RecyclerView adapter
        searchRecycler.setAdapter(recyclerAdapterSearchDetails);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getContext()).getRequestQueue();

        makeSearchResultsRequest();
        if (searchResultsList.isEmpty()){
            noResult.setVisibility(View.VISIBLE);
        }else {
            noResult.setVisibility(View.GONE);
        }

    }

    private void makeSearchResultsRequest() {


        Log.e("loaded", "" + BaseUri + "/autocompleteService/searchResult/" + Query.replaceAll("\\s",""));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUri + "/autocompleteService/searchResult/" + Query.replaceAll("\\s",""), null, new Response.Listener<JSONObject>() {
            JSONArray jsonObjectJSON = null;

            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    jsonObjectJSON = jsonObject.getJSONArray("post");

                    //clearing blogList
                    searchResultsList.clear();

                    for (int i = 0; i < jsonObjectJSON.length(); i++) {
                     /*   Log.d("error", jsonObjectJSON.toString());*/
                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            Log.e("Testing data",""+obj.getString("Industry"));

                            SearchResultsModel searchResultsModel = new SearchResultsModel(obj.getString("pid"), obj.getString("created_user"), obj.getString("companyname"), obj.getString("postuserimgname"), obj.getString("created_by"), obj.getString("post"), obj.getString("Industry"), obj.getString("companyname"));

                            searchResultsList.add(searchResultsModel);

                        } catch (Exception e) {
                            Log.d("error", e.getMessage());
                            System.out.println(e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            recyclerAdapterSearchDetails.notifyDataSetChanged();

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
}
