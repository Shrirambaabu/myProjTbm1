package igotplaced.com.layouts;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import igotplaced.com.layouts.Fragments.HomeFragment;
import igotplaced.com.layouts.Fragments.ProfileFragment;

import static igotplaced.com.layouts.Utils.Utils.BaseImageUri;

public class MainActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    String pathOfFile = BaseImageUri+"/video/iGotPlaced.mp4";
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

        navigation();

        navigationDrawer();

        HomeFragment homeFragment = new HomeFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, homeFragment);
        fragmentTransaction.commit();

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
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:

                        videoView.setVisibility(View.VISIBLE);
                        collapsingToolbar.setTitleEnabled(true);
                        isMainFragment = true;

                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentHomeTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentHomeTransaction.replace(R.id.frame, homeFragment);
                        fragmentHomeTransaction.commit();
                        return true;

                    case R.id.profile:

                        videoView.setVisibility(View.GONE);
                        collapsingToolbar.setTitleEnabled(false);
                        isMainFragment = false;

                        ProfileFragment profileFragment = new ProfileFragment();
                        android.support.v4.app.FragmentTransaction fragmentProfileTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentProfileTransaction.replace(R.id.frame, profileFragment);
                        fragmentProfileTransaction.commit();
                        return true;

 /*                     case R.id.notification:

                        videoView.setVisibility(View.GONE);
                        collapsingToolbar.setTitleEnabled(false);


                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.recent_feeds:

                        videoView.setVisibility(View.GONE);
                        collapsingToolbar.setTitleEnabled(false);


                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.recently_got_placed:

                        videoView.setVisibility(View.GONE);
                        collapsingToolbar.setTitleEnabled(false);


                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.testimonial:

                        videoView.setVisibility(View.GONE);
                        collapsingToolbar.setTitleEnabled(false);


                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.blog:

                        videoView.setVisibility(View.GONE);
                        collapsingToolbar.setTitleEnabled(false);


                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.settings:

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
                        isMainFragment = true;

                        HomeFragment homeFragmentDefault = new HomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, homeFragmentDefault);
                        fragmentTransaction.commit();
                        return true;

                }
            }
        });


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
        } else if (isMainFragment) {
            new AlertDialog.Builder(this)
                    .setTitle("Exit Confirmation")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.super.onBackPressed();
                            endApplication();
                        }
                    }).create().show();
        } else {
            isMainFragment = true;
            navigationView.getMenu().getItem(0).setChecked(true);
            fragmentManager.beginTransaction().replace(R.id.home, new HomeFragment()).commit();
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