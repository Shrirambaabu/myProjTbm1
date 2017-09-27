package igotplaced.com.layouts.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static igotplaced.com.layouts.Utils.Utils.isNetworkAvailable;

/**
 * Created by Ashith VL on 9/12/2017.
 */

public class InternetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isNetworkAvailable(context)) {
           //Toast.makeText(context, "Sorry! No Internet!!!", Toast.LENGTH_SHORT).show();
        }
    }


}