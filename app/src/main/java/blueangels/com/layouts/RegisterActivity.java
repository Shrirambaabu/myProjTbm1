package blueangels.com.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 5/2/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    private String[] passOutYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        String[] passOutYear = getResources().getStringArray(R.array.year_arrays);
        List<String> plantsList = new ArrayList<String>();

        Collections.addAll(plantsList, passOutYear);

        Spinner spinner = (Spinner) findViewById(R.id.passed_out_year_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, plantsList);
        spinner.setPrompt("  -- Select the Passout Year -- ");
        spinner.setAdapter(spinnerArrayAdapter);

    }

    public void registerNew(View view) {

        Intent registrationCompleteIntent = new Intent(RegisterActivity.this, RegisterPasswordActivity.class);
        startActivity(registrationCompleteIntent);

    }
}