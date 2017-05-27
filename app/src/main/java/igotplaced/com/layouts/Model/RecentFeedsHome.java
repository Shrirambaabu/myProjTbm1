package igotplaced.com.layouts.Model;

/**
 * Created by Ashith VL on 5/22/2017.
 */

public class RecentFeedsHome {

    private String Type;
    private String Question;
    private String IndustryName;
    private String CompanyName;
    private String ModifiedBy;
    private String Name;
    private String ImageName;

    public RecentFeedsHome() {
    }

    public RecentFeedsHome(String type, String question, String industryName, String companyName, String modifiedBy, String name, String imageName) {
        Type = type;
        Question = question;
        IndustryName = industryName;
        CompanyName = companyName;
        ModifiedBy = modifiedBy;
        Name = name;
        ImageName = imageName;
    }

    public String getType() {
        return Type;
    }

    public String getQuestion() {
        return Question;
    }

    public String getIndustryName() {
        return IndustryName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public String getName() {
        return Name;
    }

    public String getImageName() {
        return ImageName;
    }
}
