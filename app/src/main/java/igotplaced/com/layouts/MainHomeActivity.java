package igotplaced.com.layouts;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import igotplaced.com.layouts.Fragments.EventFragment;
import igotplaced.com.layouts.Fragments.InterviewFragment;
import igotplaced.com.layouts.Fragments.PostFragment;
import igotplaced.com.layouts.Fragments.QuestionsFragment;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.pushFragment;


public class MainHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        setupNavigationView();
    }

    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
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
                pushFragment(new PostFragment(),fragmentManager);
                break;
            case R.id.interview_experience_home:
                // Action to perform when interview Menu item is selected.
                pushFragment(new InterviewFragment(),fragmentManager);
                break;
            case R.id.events:
                // Action to perform when events Menu item is selected.
                pushFragment(new EventFragment(),fragmentManager);
                break;
            case R.id.questions:
                // Action to perform when questions Menu item is selected.
                pushFragment(new QuestionsFragment(),fragmentManager);
                break;
        }
    }



    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */

}
