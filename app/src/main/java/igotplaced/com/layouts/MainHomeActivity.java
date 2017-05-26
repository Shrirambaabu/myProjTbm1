package igotplaced.com.layouts;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import igotplaced.com.layouts.Fragments.HomeEventFragment;
import igotplaced.com.layouts.Fragments.HomeInterviewFragment;
import igotplaced.com.layouts.Fragments.HomePostFragment;
import igotplaced.com.layouts.Fragments.HomeQuestionsFragment;

import static igotplaced.com.layouts.Utils.Utils.pushFragment;


public class MainHomeActivity extends AppCompatActivity {

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        setupNavigationView();

        intentData();

    }

    private void intentData() {
        Intent intent = getIntent();

        if (intent.getIntExtra("bottomNavigation", 0) == 1) {

            selectFragment(menu.getItem(0));

        } else if (intent.getIntExtra("bottomNavigation", 0) == 2) {

            selectFragment(menu.getItem(1));

        } else if (intent.getIntExtra("bottomNavigation", 0) == 3) {

            selectFragment(menu.getItem(2));

        } else if (intent.getIntExtra("bottomNavigation", 0) == 4) {

            selectFragment(menu.getItem(3));
        }
    }

    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(0));

            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            selectFragment(item);
                            return false;
                        }
                    });
        }
    }

    /**
     * Perform action when any item is selected.
     *
     * @param item Item that is selected.
     */
    protected void selectFragment(MenuItem item) {

        item.setChecked(true);
        FragmentManager fragmentManager = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.post_home:
                // Action to perform when post Menu item is selected.
                pushFragment(new HomePostFragment(), fragmentManager);
                break;
            case R.id.interview_experience_home:
                // Action to perform when interview Menu item is selected.
                pushFragment(new HomeInterviewFragment(), fragmentManager);
                break;
            case R.id.events:
                // Action to perform when events Menu item is selected.
                pushFragment(new HomeEventFragment(), fragmentManager);
                break;
            case R.id.questions:
                // Action to perform when questions Menu item is selected.
                pushFragment(new HomeQuestionsFragment(), fragmentManager);
                break;
        }
    }


    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */

}
