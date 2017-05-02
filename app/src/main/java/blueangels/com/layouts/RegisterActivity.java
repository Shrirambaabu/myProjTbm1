package blueangels.com.layouts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Admin on 5/2/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.registration);
    }

}
