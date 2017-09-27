package igotplaced.com.layouts.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static igotplaced.com.layouts.Utils.Utils.isNetworkAvailable;

/**
 * Created by Shriram on 27-Sep-17.
 */

public class MyService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        Log.e("ServiceSer","Stop Service");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ConnectionCheck connectionCheck = new ConnectionCheck();
        if (connectionCheck.isNetworkConnected(this)){// or your context pass here
            //operation you want if connection available
        }else {
              //context pass here
        }
        Log.e("ServiceSer",""+Thread.currentThread().getId());
        return super.onStartCommand(intent, flags, startId);
    }
}
