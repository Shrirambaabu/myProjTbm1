package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/23/2017.
 */

public class MentorsHome {
    private String imageName;
    private String mentorName;
    private String mentorProfession;
    private String mentorCompany;
    private String mentorLinkedIn;

    public MentorsHome() {

    }

    public MentorsHome(String imageName, String mentorName, String mentorProfession, String mentorCompany, String mentorLinkedIn) {
        this.imageName = imageName;
        this.mentorName = mentorName;
        this.mentorProfession = mentorProfession;
        this.mentorCompany = mentorCompany;
        this.mentorLinkedIn = mentorLinkedIn;

    }

    public String getImageName() {
        return imageName;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getMentorProfession() {
        return mentorProfession;
    }

    public String getMentorCompany() {
        return mentorCompany;
    }

    public String getMentorLinkedIn() {
        return mentorLinkedIn;
    }

}







