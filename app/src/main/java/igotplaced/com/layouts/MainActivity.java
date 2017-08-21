package igotplaced.com.layouts;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import igotplaced.com.layouts.Fragments.AboutUsFragment;
import igotplaced.com.layouts.Fragments.BlogFragment;
import igotplaced.com.layouts.Fragments.HomeFragment;
import igotplaced.com.layouts.Fragments.NotificationFragment;
import igotplaced.com.layouts.Fragments.ProfileFragment;
import igotplaced.com.layouts.Fragments.SearchResults;
import igotplaced.com.layouts.Model.Profile;
import igotplaced.com.layouts.Utils.CustomAutoCompleteView;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;
import igotplaced.com.layouts.Utils.Validation;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Email;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class MainActivity extends AppCompatActivity {


    private SimpleCursorAdapter mAdapter;


    JSONArray terms;


    //Defining Variables
    private Toolbar toolbar;
    private Context context;
    private RequestQueue queue;

    private TextView nameHeader, emailHeader;
    private NetworkImageView profile_img;

    private ArrayAdapter<String> searchAutoCompleteAdapter;
    SearchView searchView;
    /* private CustomAutoCompleteView searchEditText;
     private TextInputLayout inputLayoutSearch;*/
    private DrawerLayout drawerLayout;
    private boolean isMain = false;
    private SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private String userName, userEmail, userId = null;
    int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        context = getApplicationContext();

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userEmail = sharedpreferences.getString(Email, null);
        userId = sharedpreferences.getString(Id, null);


        queue = NetworkController.getInstance(context).getRequestQueue();


        navigation();
        makeJsonArrayRequestProfile();
        navigationDrawer();


        displaySelectedScreen(R.id.home);

    }

    private void makeJsonArrayRequestProfile() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/profileService/profileEdit/" + userId, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);


                        Profile profileHome = new Profile(obj.getString("imgname"), obj.getString("fname"), obj.getString("email"));

                        nameHeader.setText(profileHome.getProfileName());
                        emailHeader.setText(profileHome.getEmail());


                        Log.d("error", "Error: " + Utils.BaseImageUri + profileHome.getImageName());
                        profile_img.setImageUrl(Utils.BaseImageUri + profileHome.getImageName(), NetworkController.getInstance(context).getImageLoader());


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
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

    private void navigation() {

        //Initializing NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.home);
        View header = navigationView.getHeaderView(0);

        nameHeader = (TextView) header.findViewById(R.id.name);
        emailHeader = (TextView) header.findViewById(R.id.email);
        profile_img = (NetworkImageView) header.findViewById(R.id.img_profile);


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation men
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                displaySelectedScreen(menuItem.getItemId());

                return true;
            }
        });


    }


    private void displaySelectedScreen(int itemId) {
        //Check to see which item was being clicked and perform appropriate action
        Fragment fragment = null;
        switch (itemId) {

            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.home:
                isMain = true;
                selectedPosition = 0;
                fragment = new HomeFragment();

                Log.e("SharedValue ;", "");
                break;
            case R.id.profile:
                isMain = false;
                selectedPosition = 1;
                fragment = new ProfileFragment();
                break;

            case R.id.notification:
                isMain = false;
                selectedPosition = 2;
                fragment = new NotificationFragment();
                break;


            case R.id.blog:
                isMain = false;
                selectedPosition = 3;
                fragment = new BlogFragment();
                break;

            case R.id.about_us:
                selectedPosition = 4;
                fragment = new AboutUsFragment();
                break;

            case R.id.log_out:
                editor = sharedpreferences.edit();
                editor.putString(Email, null);
                editor.putString(Id, null);
                editor.putString(Name, null);
                editor.commit();
                Intent logOut = new Intent(this, LoginActivity.class);
                logOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logOut);
                break;


 /*                      case R.id.settings:

                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.privacy_policy:


                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.terms_and_conditions:

                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;*/

            default:
                isMain = true;
                selectedPosition = 0;
                fragment = new HomeFragment();
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        }
    }


    public void navigationDrawer() {

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void endApplication() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (selectedPosition != 0) {
            displaySelectedScreen(R.id.home);
        } else {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                        }
                    }).setNegativeButton("No", null).show();

        }


    }

    private void networkSearchAutoCompleteRequest(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/autocompleteService/searchAll/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                terms = response;
                searchAutoCompleteAdapter.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        searchAutoCompleteAdapter.add(String.valueOf(response.get(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                //  Utils.showDialogue(MainActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 3000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(jsonArrayRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.serach, menu);
        // Retrieve the SearchView and plug it into SearchManager
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mAdapter = new SimpleCursorAdapter(
                context, android.R.layout.simple_list_item_1, null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1}, 1);

        searchView.setSuggestionsAdapter(mAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >= 1) {

                   /* Intent intent = new Intent(MainActivity.this, ProfilePostDetailsActivity.class);
                    intent.setAction(Intent.ACTION_SEARCH);
                    intent.putExtra(SearchManager.QUERY, query);
                    Log.e("QuerySubmit",""+query);
                    startActivity(intent);*/

                    SearchResults searchResults = new SearchResults();
                    Bundle bundle = new Bundle();
                    bundle.putString("QuerySubmit", query);
                    searchResults.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.rootLayout, searchResults, "tag")
                            .addToBackStack("tag").commit();

                    searchView.getSuggestionsAdapter().changeCursor(null);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() >= 1) {
                    networkSearchAutoCompleteRequest(query);
                    if (!query.isEmpty()) {
                        populateAdapter(query);
                        Log.e("QueryChange", "" + query);
                    }
                } else {
                    searchView.getSuggestionsAdapter().changeCursor(null);
                }

                return true;
            }
        });


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                String term = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
                cursor.close();

                Log.e("Cursor select", "" + term);
                SearchResults searchResults = new SearchResults();
                Bundle bundle = new Bundle();
                bundle.putString("QuerySubmit", term);
                searchResults.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.rootLayout, searchResults, "tag")
                        .addToBackStack("tag").commit();
                searchView.setQuery("",false); //clear the text


/*
                Intent intent = new Intent(MainActivity.this, ProfilePostDetailsActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, term);
                Log.e("Term", "" + term);
                startActivity(intent);*/
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return onSuggestionSelect(position);
            }
        });

        searchAutoCompleteAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);
        networkSearchAutoCompleteRequest("INTERNET");

        return true;
    }

    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1});

        if (!query.isEmpty()) {

            networkSearchAutoCompleteRequest(query);

            for (int index = 0; index < terms.length(); index++) {
                String term = null;
                try {
                    term = terms.getString(index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Object[] row = new Object[]{index, term};
                c.addRow(row);
            }
        }
        mAdapter.changeCursor(c);
    }
}