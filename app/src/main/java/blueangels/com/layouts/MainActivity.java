package blueangels.com.layouts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void forgetPassword(View view) {
        Intent intent =new Intent(MainActivity.this,ForgetPassword.class);
    }

    public void login(View view) {
        Intent intent = new Intent(MainActivity.this,Login.class);
    }

    public void register(View view) {
        Intent intent = new Intent(MainActivity.this,Register.class);
    }
}
