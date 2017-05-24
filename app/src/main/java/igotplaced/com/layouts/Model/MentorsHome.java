package igotplaced.com.layouts.Model;

import java.lang.ref.SoftReference;

/**
 * Created by Admin on 5/23/2017.
 */

public class MentorsHome {
    private String ImageName;
    private String MentorName;
    private String MentorProfession;
    private String MentorCompany;
    private String MentorLinkedIn;

    public MentorsHome() {

    }

    public MentorsHome(String imageName, String mentorName, String mentorProfession, String mentorCompany, String mentorLinkedIn) {
        ImageName = imageName;
        MentorName = mentorName;
        MentorProfession = mentorProfession;
        MentorCompany = mentorCompany;
        MentorLinkedIn = mentorLinkedIn;

    }

    public String getImageName() {
        return ImageName;
    }

    public String getMentorName() {
        return MentorName;
    }

    public String getMentorProfession() {
        return MentorProfession;
    }

    public String getMentorCompany() {
        return MentorCompany;
    }

    public String getMentorLinkedIn() {
        return MentorLinkedIn;
    }

}







