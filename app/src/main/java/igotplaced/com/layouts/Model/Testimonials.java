package igotplaced.com.layouts.Model;

import android.provider.ContactsContract;

/**
 * Created by Admin on 5/23/2017.
 */

public class Testimonials {
    private String ProfileMessage;
    private String ProfileName;
    private String ProfileCollege;
    private String ImageName;

    public Testimonials(){

    }
    public Testimonials(String profileMessage,String profileName,String profileCollege,String imageName){

        ProfileMessage=profileMessage;
        ProfileName=profileName;
        ProfileCollege=profileCollege;
        ImageName=imageName;

    }

    public String getProfileMessage(){return ProfileMessage;}
    public String getProfileName(){return ProfileName;}
    public String getProfileCollege(){return ProfileCollege;}
    public String getImageName(){return ImageName;}
}
