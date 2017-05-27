package igotplaced.com.layouts.Model;

/**
 * Created by Ashith VL on 5/22/2017.
 */

public class RecentFeedsHome {

    private String type;
    private String Question;
    private String industryName;
    private String companyName;
    private String modifiedBy;
    private String name;
    private String imageName;

    public RecentFeedsHome() {
    }

    public RecentFeedsHome(String type, String question, String industryName, String companyName, String modifiedBy, String name, String imageName) {
        this.type = type;
        Question = question;
        this.industryName = industryName;
        this.companyName = companyName;
        this.modifiedBy = modifiedBy;
        this.name = name;
        this.imageName = imageName;
    }

    public String getType() {
        return type;
    }

    public String getQuestion() {
        return Question;
    }

    public String getIndustryName() {
        return industryName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }
}
