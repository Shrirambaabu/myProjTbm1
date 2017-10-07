package igotplaced.com.layouts.Utils;

/**
 * Created by Shriram on 07-Oct-17.
 */

import android.app.Application;
import android.content.Context;



public class AppManager extends Application {

    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


    private static AppManager mAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    public AppManager() {
        mAppContext = this;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

}