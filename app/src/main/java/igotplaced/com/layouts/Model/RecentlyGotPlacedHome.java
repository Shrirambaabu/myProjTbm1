package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/23/2017.
 */

public class RecentlyGotPlacedHome {
    private String ImageName;
    private String PersonName;
    private String PlacedDetails;

    public RecentlyGotPlacedHome() {

    }

    public RecentlyGotPlacedHome(String imageName, String personName, String placedDetails){
        ImageName=imageName;
        PersonName=personName;
        PlacedDetails=placedDetails;
    }
public String getImageName(){return ImageName;}
    public String getPersonName(){return PersonName;}
    public String getPlacedDetails(){return PlacedDetails;}

}