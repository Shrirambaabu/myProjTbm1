package igotplaced.com.layouts.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.BottomNavigationViewHelper;

import static igotplaced.com.layouts.Utils.Utils.pushFragment;


public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //getting context

        setupNavigationView(view);

        return view;
    }


    private void setupNavigationView(View view) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
