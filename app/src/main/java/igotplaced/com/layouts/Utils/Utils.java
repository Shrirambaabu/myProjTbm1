package igotplaced.com.layouts.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import igotplaced.com.layouts.R;

/**
 * Created by Ashith VL on 5/7/2017.
 */

public class Utils {
    public static String Base = "http://192.168.0.105:8080";
    //public static String Base = "http://49.207.183.11:8080";
   // public static String Base = "http://servicetier.jvmhost.net";

    public static String BaseUri = Base + "/IgotplacedRestWebService/webapi";
    public static String BaseImageUri = "http://igotplaced.com";

    public static final String MyPREFERENCES = "igp";
    public static final String Name = "nameKey";
    public static final String Id = "idKey";
    public static final String Email = "emailKey";
    public static final String UserImage = "imageKey";

    public static void setSpinnerError(Spinner spinner, String error, Activity activity) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent)); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            // spinner.performClick(); to open the spinner list if error is found.
        }
    }

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return false; // It's a light color
        } else {
            return true; // It's a dark color
        }
    }

    public static boolean checkConnection(View view, Activity activity) {
        // showSnack(isConnected, view, activity);
        return ConnectivityReceiver.isConnected();
    }

    // Showing the status in Snackbar
    public static void showSnack(boolean isConnected, View view, Activity activity) {

        if (!isConnected) {
            String message = "Sorry! Not connected to internet";
            int color = Color.WHITE;

            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            sbView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorBackground));
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    public static void showSnack(View view, Activity activity, String message) {

        int color = Color.WHITE;

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        sbView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorBackground));
        textView.setTextColor(color);
        sbView.setBackground(ContextCompat.getDrawable(activity, R.color.colorBackground));
        snackbar.show();
    }

    public static void showCustomToast(Activity activity, String message, LayoutInflater inflater) {

        View customToastRoot = inflater.inflate(R.layout.my_custom_toast, null);

        Toast customToast = new Toast(activity);

        customToast.setView(customToastRoot);
        customToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        customToast.setDuration(Toast.LENGTH_LONG);

        TextView textView = (TextView) customToastRoot.findViewById(R.id.toast_text_view);
        textView.setText(message);

        customToast.show();

    }
    public static void menuIconColor(Drawable drawable, int color, Activity activity) {
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(activity, color), PorterDuff.Mode.SRC_ATOP);
        }
    }
    public static void showDialogue(final Activity activity, String message) {

        if (message.equals("Sorry! Not connected to internet")) {

            final Dialog alertDialogBuilder = new Dialog(activity);
            alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialogBuilder.setContentView(R.layout.main_dialog);


            TextView textMain = (TextView) alertDialogBuilder.findViewById(R.id.main_message);
            final SwitchCompat switchCompat = (SwitchCompat) alertDialogBuilder.findViewById(R.id.toggle_button);
            final SwitchCompat switchMobile = (SwitchCompat) alertDialogBuilder.findViewById(R.id.toggle_button2);


            textMain.setText(message);

            final WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switch (buttonView.getId()) {

                        case R.id.toggle_button:
                            if (isChecked) {
                                wifiManager.setWifiEnabled(true);
                                alertDialogBuilder.dismiss();
                            } else {
                                wifiManager.setWifiEnabled(false);
                            }
                            break;
                    }


                }
            });

            switchMobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.toggle_button2:
                            if (isChecked) {
                                activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                alertDialogBuilder.dismiss();
                            }
                    }
                }
            });

            alertDialogBuilder.show();
        } else {
            final Dialog alertDialogBuilder = new Dialog(activity);
            alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialogBuilder.setContentView(R.layout.other_dialog);
            TextView textMain = (TextView) alertDialogBuilder.findViewById(R.id.main_message);
            TextView close = (TextView) alertDialogBuilder.findViewById(R.id.close);
            textMain.setText(message);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogBuilder.dismiss();
                }
            });
            alertDialogBuilder.show();
        }

    }


    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnected() && connectGoogle()) {
                Log.e("error", "no Internet");
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    //To Test whether the Internet Connection is fast enough
    private static boolean connectGoogle() {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(10000);
            urlc.connect();
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            Log.e("error", "no Google");
            return false;
        }
    }

    public static void pushFragment(Fragment fragment, FragmentManager fragmentManager) {
        if (fragment == null)
            return;
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.home_frame, fragment);
                ft.commit();
            }
        }
    }

    public static double screenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }

}
