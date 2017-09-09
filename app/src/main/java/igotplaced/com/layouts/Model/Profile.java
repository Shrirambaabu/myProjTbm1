package igotplaced.com.layouts.Model;

/**
 * Created by Ashith VL on 6/2/2017.
 */

public class Profile {

    private String imageName;
    private String profileName;
    private String departmentName;
    private String collegeName;
    private String email;
    private String yearOfPassOut;
    private String interest;
    private String mobileNumber;
    private String location;
    private String industry1;
    private String industry2;
    private String industry3;
    private String company1;
    private String company2;
    private String company3;
    private String companyAdapterOne;
    private String companyAdapterTwo;
    private String companyAdapterThree;

    public Profile(String imageName, String profileName, String departmentName, String collegeName, String email, String yearOfPassOut, String interest, String mobileNumber, String location, String industry1, String industry2, String industry3, String company1, String company2, String company3, String companyAdapterOne, String companyAdapterTwo, String companyAdapterThree) {
        this.imageName = imageName;
        this.profileName = profileName;
        this.departmentName = departmentName;
        this.collegeName = collegeName;
        this.email = email;
        this.yearOfPassOut = yearOfPassOut;
        this.interest = interest;
        this.mobileNumber = mobileNumber;
        this.location = location;
        this.industry1 = industry1;
        this.industry2 = industry2;
        this.industry3 = industry3;
        this.company1 = company1;
        this.company2 = company2;
        this.company3 = company3;
        this.companyAdapterOne = companyAdapterOne;
        this.companyAdapterTwo = companyAdapterTwo;
        this.companyAdapterThree = companyAdapterThree;
    }

    public Profile(String imageName, String profileName, String email) {
        this.imageName = imageName;
        this.profileName = profileName;
        this.email = email;
    }

    public Profile() {
    }

    public Profile(String imageName, String profileName, String departmentName, String collegeName, String email, String yearOfPassOut, String interest, String mobileNumber, String location, String industry1, String industry2, String industry3, String company1, String company2, String company3) {
        this.imageName = imageName;
        this.profileName = profileName;
        this.departmentName = departmentName;
        this.collegeName = collegeName;
        this.email = email;
        this.yearOfPassOut = yearOfPassOut;
        this.interest = interest;
        this.mobileNumber = mobileNumber;
        this.location = location;
        this.industry1 = industry1;
        this.industry2 = industry2;
        this.industry3 = industry3;
        this.company1 = company1;
        this.company2 = company2;
        this.company3 = company3;
    }

    public String getImageName() {
        return imageName;
    }
    public String getCompanyAdapterOne() {
        return companyAdapterOne;
    }

    public String getCompanyAdapterTwo() {
        return companyAdapterTwo;
    }

    public String getCompanyAdapterThree() {
        return companyAdapterThree;
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

    public String getEmail() {
        return email;
    }

    public String getYearOfPassOut() {
        return yearOfPassOut;
    }

    public String getInterest() {
        return interest;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getIndustry1() {
        return industry1;
    }

    public String getIndustry2() {
        return industry2;
    }

    public String getIndustry3() {
        return industry3;
    }

    public String getCompany1() {
        return company1;
    }

    public String getCompany2() {
        return company2;
    }

    public String getCompany3() {
        return company3;
    }
}
