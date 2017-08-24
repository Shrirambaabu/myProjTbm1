package igotplaced.com.layouts.Model;

/**
 * Created by Shriram on 24-Aug-17.
 */

public class Company {

    private String companyId;
    private String companyName;
    private String companyWebsite;
    private String companyImage;
    private String companyAbout;

    public Company(String companyId, String companyName, String companyWebsite, String companyImage, String companyAbout) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyWebsite = companyWebsite;
        this.companyImage = companyImage;
        this.companyAbout = companyAbout;
    }

    public Company() {
    }


    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public String getCompanyAbout() {
        return companyAbout;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    public void setCompanyAbout(String companyAbout) {
        this.companyAbout = companyAbout;
    }

}
