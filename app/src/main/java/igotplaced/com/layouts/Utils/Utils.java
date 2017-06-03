package igotplaced.com.layouts.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import igotplaced.com.layouts.R;

/**
 * Created by Ashith VL on 5/7/2017.
 */

public class Utils {
   public static String Base = "http://10.238.65.246:8080";

    public static String BaseUri = Base+"/IgotplacedRestWebService/webapi";
    public static String BaseImageUri = "http://igotplaced.com";

    public static final String MyPREFERENCES = "igp" ;
    public static final String Name = "nameKey";
    public static final String Id = "idKey";
    public static final String Email = "emailKey";

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

    public static void showDialogue(final Activity activity, String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("OOPS!!! Error");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void pushFragment(Fragment fragment, FragmentManager fragmentManager) {
        if (fragment == null)
            return;
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
    }

}
