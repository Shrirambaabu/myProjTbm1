package igotplaced.com.layouts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import static igotplaced.com.layouts.Utils.Utils.Email;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

/**
 * Created by Admin on 5/2/2017.
 */
public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer is set to 2 seconds(in milliseconds)
    private static int SPLASH_TIME_OUT = 2000;

    //Called when the activity is first created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If your minSdkVersion is 11 or higher, instead use:
        //getActionBar().hide();
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        //Set the activity content from a layout resource
        setContentView(R.layout.activity_splash_screen);

        //posting Runnable object to the thread's MessageQueue
        //to schedule runnable to be executed as some point in the future
        //postDelayed(Runnable, long)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will be executed once the timer is over
                //Start your app main activity
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String userName = sharedpreferences.getString(Name, null);
                String userId = sharedpreferences.getString(Id, null);
                String userEmail = sharedpreferences.getString(Email, null);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.commit();
                if (userName == null || userId == null || userEmail == null) {
                    Intent loginIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (screenSize(SplashScreenActivity.this) > 6.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}