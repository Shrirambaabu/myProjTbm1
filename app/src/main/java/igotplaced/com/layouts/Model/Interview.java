package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/30/2017.
 */

public class Interview {
    private String interviewId;
    private String interviewUserId;
    private String interview;
    private String interviewIndustry;
    private String interviewImage;
    private String interviewProfileName;
    private String interviewTime;
    private String userImage;
    private String userComment;

    public Interview() {
    }

    public Interview(String interviewId, String interviewUserId, String interview, String interviewIndustry, String interviewImage, String interviewProfileName, String interviewTime, String userImage, String userComment) {
        this.interviewId = interviewId;
        this.interviewUserId = interviewUserId;
        this.interview = interview;
        this.interviewIndustry = interviewIndustry;
        this.interviewImage = interviewImage;
        this.interviewProfileName = interviewProfileName;
        this.interviewTime = interviewTime;
        this.userImage = userImage;
        this.userComment = userComment;
    }


    public Interview(String interview, String interviewIndustry, String interviewImage, String interviewProfileName, String interviewTime, String userComment) {
        this.interview = interview;
        this.interviewIndustry = interviewIndustry;
        this.interviewImage = interviewImage;
        this.interviewProfileName = interviewProfileName;
        this.interviewTime = interviewTime;
        this.userComment = userComment;
    }

    public Interview(String interview, String interviewIndustry, String interviewImage, String interviewProfileName, String interviewTime, String userImage, String userComment) {
        this.interview = interview;
        this.interviewIndustry = interviewIndustry;
        this.interviewImage = interviewImage;
        this.interviewProfileName = interviewProfileName;
        this.interviewTime = interviewTime;
        this.userImage = userImage;
        this.userComment = userComment;
    }

    public String getInterviewId() {
        return interviewId;
    }

    public String getInterviewUserId() {
        return interviewUserId;
    }

    public String getInterview() {
        return interview;
    }

    public String getInterviewIndustry() {
        return interviewIndustry;
    }

    public String getInterviewImage() {
        return interviewImage;
    }

    public String getInterviewProfileName() {
        return interviewProfileName;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserComment() {
        return userComment;
    }
}
