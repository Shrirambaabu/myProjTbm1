package igotplaced.com.layouts.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Shriram on 27-Sep-17.
 */

public class ConnectionCheck {

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            Log.e("Net","True");
            // There are no active networks.
            return false;
        } else
            Log.e("Net","Faa");
            return true;
    }
}
