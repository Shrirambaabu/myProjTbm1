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
    ArrayList<Integer> list = new ArrayList();
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

        //  noResult = (TextView) view.findViewById(R.id.no_result);

        searchRecyclerView(view);

        return view;
    }

    private void searchRecyclerView(View view) {

        RecyclerView searchRecycler = (RecyclerView) view.findViewById(R.id.recycler_view_search_results);


        recyclerAdapterSearchDetails = new RecyclerAdapterSearchDetails(getContext(), searchResultsList, list);

        Log.e("ListCall", "" + searchResultsList.size());
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
       /* if (searchResultsList.isEmpty()){
            noResult.setVisibility(View.VISIBLE);
        }else {
            noResult.setVisibility(View.GONE);
        }*/

    }

    private void makeSearchResultsRequest() {


        Log.e("loaded", "" + BaseUri + "/autocompleteService/searchResult/" + Query.replaceAll("\\s", ""));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUri + "/autocompleteService/searchResult/" + Query.replaceAll("\\s", ""), null, new Response.Listener<JSONObject>() {
            JSONArray jsonObjectJSON = null;

            @Override
            public void onResponse(JSONObject jsonObject) {

                //clearing blogList
                searchResultsList.clear();
                try {
                    jsonObjectJSON = jsonObject.getJSONArray("post");


                    for (int i = 0; i < jsonObjectJSON.length(); i++) {
                     /*   Log.d("error", jsonObjectJSON.toString());*/
                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            Log.e("Testing data Post", "" + obj.getString("pid"));

                            SearchResultsModel searchResultsModel = new SearchResultsModel( );
                            searchResultsModel.setId(obj.getString("pid"));
                            searchResultsModel.setUserId(obj.getString("created_user"));
                            searchResultsModel.setUserName( obj.getString("created_uname"));
                            searchResultsModel.setUserImage(obj.getString("postuserimgname"));
                            searchResultsModel.setCreatedDate(obj.getString("created_by"));
                            searchResultsModel.setMessage( obj.getString("post"));
                            searchResultsModel.setIndustry(obj.getString("Industry"));
                            searchResultsModel.setCompany(obj.getString("companyname"));
                            searchResultsModel.setCompanyId(obj.getString("company_id"));



                            searchResultsList.add(searchResultsModel);
                            list.add(0);
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

                try {
                    jsonObjectJSON = jsonObject.getJSONArray("intexp");


                    for (int i = 0; i < jsonObjectJSON.length(); i++) {
                     /*   Log.d("error", jsonObjectJSON.toString());*/
                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            Log.e("Testing data Interview", "" + obj.getString("id"));

                            SearchResultsModel searchResultsModel = new SearchResultsModel( );
                            searchResultsModel.setId(obj.getString("id"));
                            searchResultsModel.setUserId(obj.getString("user_id"));
                            searchResultsModel.setUserName( obj.getString("username"));
                            searchResultsModel.setUserImage(obj.getString("interviewUserImgName"));
                            searchResultsModel.setCreatedDate(obj.getString("created_by"));
                            searchResultsModel.setMessage( obj.getString("feedback"));
                            searchResultsModel.setIndustry(obj.getString("industryname"));
                            searchResultsModel.setCompany(obj.getString("companyname"));
                            searchResultsModel.setCompanyId(obj.getString("company_id"));
                            searchResultsList.add(searchResultsModel);

                            list.add(1);
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
                try {
                    jsonObjectJSON = jsonObject.getJSONArray("events");


                    for (int i = 0; i < jsonObjectJSON.length(); i++) {
                     /*   Log.d("error", jsonObjectJSON.toString());*/
                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            Log.e("Testing data Events", "" + obj.getString("id"));

                            SearchResultsModel searchResultsModel = new SearchResultsModel();

                            searchResultsModel.setId(obj.getString("id"));
                            searchResultsModel.setUserId(obj.getString("created_user"));
                            searchResultsModel.setUserName( obj.getString("created_uname"));
                            searchResultsModel.setUserImage(obj.getString("eventImgName"));
                            searchResultsModel.setCreatedDate(obj.getString("created_by"));
                            searchResultsModel.setEventCaption(obj.getString("eventname"));
                            searchResultsModel.setEventType(obj.getString("eventtype"));
                            searchResultsModel.setEventLocation(obj.getString("location"));
                            searchResultsModel.setEventDateTime(obj.getString("datetime"));
                            searchResultsModel.setEventCount( obj.getString("count"));
                            searchResultsModel.setEventStatus(obj.getString("event"));
                            searchResultsModel.setMessage( obj.getString("notes"));
                            searchResultsModel.setIndustry(obj.getString("Industry"));
                            searchResultsModel.setCompany(obj.getString("companyname"));



                            searchResultsList.add(searchResultsModel);
                            list.add(2);
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
                try {
                    jsonObjectJSON = jsonObject.getJSONArray("questions");


                    for (int i = 0; i < jsonObjectJSON.length(); i++) {
                     /*   Log.d("error", jsonObjectJSON.toString());*/
                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            Log.e("Testing data Questions", "" + obj.getString("id"));

                            SearchResultsModel searchResultsModel = new SearchResultsModel();

                            searchResultsModel.setId(obj.getString("id"));
                            searchResultsModel.setUserId(obj.getString("created_user"));
                            searchResultsModel.setUserName( obj.getString("created_uname"));
                            searchResultsModel.setUserImage(obj.getString("questionUserImgName"));
                            searchResultsModel.setCreatedDate(obj.getString("created_by"));
                            searchResultsModel.setMessage( obj.getString("question"));
                            searchResultsModel.setIndustry(obj.getString("industryname"));
                            searchResultsModel.setCompany(obj.getString("companyname"));
                            searchResultsModel.setCompanyId(obj.getString("company_id"));

                            searchResultsList.add(searchResultsModel);
                            list.add(3);
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
                for (int i=0;i<searchResultsList.size();i++){
                    Log.e("Total Id",""+searchResultsList.get(i).getId()+" Size "+searchResultsList.size());
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
