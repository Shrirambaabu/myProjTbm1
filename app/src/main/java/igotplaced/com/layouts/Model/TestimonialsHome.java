package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/23/2017.
 */

public class TestimonialsHome {
    private String profileMessage;
    private String profileName;
    private String profileCollege;
    private String imageName;

    public TestimonialsHome(){

    }
    public TestimonialsHome(String profileMessage, String profileName, String profileCollege, String imageName){

        this.profileMessage =profileMessage;
        this.profileName =profileName;
        this.profileCollege =profileCollege;
        this.imageName =imageName;

    }

    public String getProfileMessage(){return profileMessage;}
    public String getProfileName(){return profileName;}
    public String getProfileCollege(){return profileCollege;}
    public String getImageName(){return imageName;}
}
