package blueangels.com.layouts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ashith VL on 5/2/2017.
 */

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_forget_password);
    }

}
