package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/29/2017.
 */

public class ProfileHome {

    private String imageName;
    private String profileName;
    private String departmentName;
    private String collegeName;

    public ProfileHome() {
    }

    public ProfileHome(String imageName, String profileName, String departmentName, String collegeName) {
        this.imageName = imageName;
        this.profileName = profileName;
        this.departmentName = departmentName;
        this.collegeName = collegeName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getCollegeName() {
        return collegeName;
    }
}
