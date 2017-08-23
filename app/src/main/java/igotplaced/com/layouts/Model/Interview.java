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
    private String comentUserImage;
    private String commentMessage;
private String interviewCompany;

    public String getInterviewCompany() {
        return interviewCompany;
    }

    public Interview() {
    }

    public Interview(String comentUserImage, String commentMessage) {
        this.comentUserImage = comentUserImage;
        this.commentMessage = commentMessage;
    }

    public Interview(String interviewId, String interviewUserId, String interview, String interviewIndustry, String interviewImage, String interviewProfileName, String interviewTime, String userImage, String userComment,String interviewCompany) {
        this.interviewId = interviewId;
        this.interviewUserId = interviewUserId;
        this.interview = interview;
        this.interviewIndustry = interviewIndustry;
        this.interviewImage = interviewImage;
        this.interviewProfileName = interviewProfileName;
        this.interviewTime = interviewTime;
        this.userImage = userImage;
        this.userComment = userComment;
        this.interviewCompany = interviewCompany;
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

    public String getComentUserImage() {
        return comentUserImage;
    }

    public String getCommentMessage() {
        return commentMessage;
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
