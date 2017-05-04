package blueangels.com.layouts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static blueangels.com.layouts.R.id.parent;

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

        Intent registerationCompleteIntent = new Intent(RegisterActivity.this, RegisterPasswordActivity.class);
        startActivity(registerationCompleteIntent);

    }
}