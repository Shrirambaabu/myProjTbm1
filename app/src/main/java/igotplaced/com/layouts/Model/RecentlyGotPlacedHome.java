package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/23/2017.
 */

public class RecentlyGotPlacedHome {
    private String imageName;
    private String personName;
    private String placedDetails;

    public RecentlyGotPlacedHome() {

    }

    public RecentlyGotPlacedHome(String imageName, String personName, String placedDetails){
        this.imageName =imageName;
        this.personName =personName;
        this.placedDetails =placedDetails;
    }
public String getImageName(){return imageName;}
    public String getPersonName(){return personName;}
    public String getPlacedDetails(){return placedDetails;}

}