package igotplaced.com.layouts.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Ashith VL on 5/11/2017.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    private static boolean firstConnect = true;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (AppManager.isActivityVisible()) {
            if (activeNetwork == null) {
                if (firstConnect) {
                    boolean isConnected = activeNetwork != null
                            && activeNetwork.isConnectedOrConnecting();

                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
                    }
                    firstConnect = false;
                } else {
                    firstConnect = false;
                }
            } else {
                firstConnect = true;
            }
        }
    }


    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}