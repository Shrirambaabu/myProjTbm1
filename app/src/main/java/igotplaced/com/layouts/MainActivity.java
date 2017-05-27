package igotplaced.com.layouts;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import igotplaced.com.layouts.Fragments.BlogFragment;
import igotplaced.com.layouts.Fragments.HomeFragment;
import igotplaced.com.layouts.Fragments.NotificationFragment;
import igotplaced.com.layouts.Fragments.ProfileFragment;

import static igotplaced.com.layouts.Utils.Utils.BaseImageUri;

public class MainActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    String pathOfFile = BaseImageUri + "/video/iGotPlaced.mp4";
    private VideoView videoView;
    private MediaController mediaController;
    private Boolean isMainFragment;
    private FragmentManager fragmentManager;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            isMainFragment = true;
            fragmentManager.beginTransaction().replace(R.id.home, new HomeFragment()).commit();
        }
*/
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        videoView = (VideoView) findViewById(R.id.video_header);

        mediaController = new MediaController(MainActivity.this);
        mediaController.setAnchorView(videoView);
        mediaController.hide();
        Uri uri = Uri.parse(pathOfFile);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);

        videoView.start();




        /*mediaController = new MediaController(MainActivity.this);
        mediaController.setAnchorView(videoView);
        mediaController.hide();
        Uri uri = Uri.parse(pathOfFile);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.seekTo(9000);
        videoView.start();

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                mediaController.show();
            }
        });*/

        toolBar();

        navigationDrawer();

        navigation();

        displaySelectedScreen(R.id.home);


/*
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
    }

    private void toolBar() {

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("iGotPlaced");
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

    }

    private void navigation() {

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.home);

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
        Fragment fragment = null;

        //Check to see which item was being clicked and perform appropriate action
        switch (itemId) {

            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.home:

                videoView.setVisibility(View.VISIBLE);
                collapsingToolbar.setTitleEnabled(true);

                fragment = new HomeFragment();
                break;
            case R.id.profile:

                videoView.setVisibility(View.GONE);
                collapsingToolbar.setTitleEnabled(false);

                fragment = new ProfileFragment();
                break;

            case R.id.notification:

                videoView.setVisibility(View.GONE);
                collapsingToolbar.setTitleEnabled(false);

                fragment = new NotificationFragment();
                break;


            case R.id.blog:

                videoView.setVisibility(View.GONE);
                collapsingToolbar.setTitleEnabled(false);


                fragment = new BlogFragment();
                break;


 /*                      case R.id.settings:

                        videoView.setVisibility(View.GONE);
                        collapsingToolbar.setTitleEnabled(false);


                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.privacy_policy:

                        videoView.setVisibility(View.GONE);
                        collapsingToolbar.setTitleEnabled(false);


                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.terms_and_conditions:

                        videoView.setVisibility(View.GONE);
                        collapsingToolbar.setTitleEnabled(false);


                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;*/

            default:
                videoView.setVisibility(View.GONE);
                collapsingToolbar.setTitleEnabled(false);

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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.serach, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

}