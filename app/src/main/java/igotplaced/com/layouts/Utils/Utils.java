package igotplaced.com.layouts.Utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import igotplaced.com.layouts.R;

/**
 * Created by Ashith VL on 5/7/2017.
 */

public class Utils {

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
}
