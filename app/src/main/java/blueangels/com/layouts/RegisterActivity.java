package blueangels.com.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Admin on 5/2/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_registration);
    }

    public void registerNew(View view) {

        Intent registerationCompleteIntent = new Intent(RegisterActivity.this,RegisterPasswordActivity.class);
        startActivity(registerationCompleteIntent);

    }
}
