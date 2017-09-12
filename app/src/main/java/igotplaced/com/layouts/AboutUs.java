package igotplaced.com.layouts;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AboutUs extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private AppCompatEditText emailAddress;
    private TextView phoneNumber;
    private Button subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        settingToolbar();
       addressingView();
        addingListeners();
    }

    private void addingListeners() {

        subscribe.setOnClickListener(this);
        phoneNumber.setOnClickListener(this);
    }

    private void addressingView() {

        emailAddress = (AppCompatEditText) findViewById(R.id.editTextSubscribeEmail);
        phoneNumber = (TextView) findViewById(R.id.whatsApp11);
        subscribe = (Button) findViewById(R.id.subscribeButton);
    }

    private void settingToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("About us");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emailAddress:
                emailAddress.setText("");
                Toast.makeText(AboutUs.this, "Thank you for your Subscription", Toast.LENGTH_LONG).show();
                break;
            case R.id.whatsApp11:
                String phone = phoneNumber.getText().toString();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", phone, null));
                startActivity(phoneIntent);
        }
    }
}
