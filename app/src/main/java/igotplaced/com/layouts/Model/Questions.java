package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/30/2017.
 */

public class Questions {

    private String commentUserImage;
    private String commentUserMessage;

    private String questionId;
    private String questionUserId;
    private String questions;
    private String questionsIndustry;
    private String questionsImage;
    private String questionsProfileName;
    private String questionsTime;
    private String commentProfileImage;
    private String userQuestionComments;
    private String questionsCompany;

    public String getQuestionsCompany() {
        return questionsCompany;
    }

    public Questions() {
    }
    public Questions(String questionId, String questionUserId, String questions, String questionsIndustry, String questionsImage, String questionsProfileName, String questionsTime, String commentProfileImage, String userQuestionComments,String questionsCompany) {
        this.questionId = questionId;
        this.questionUserId = questionUserId;
        this.questions = questions;
        this.questionsIndustry = questionsIndustry;
        this.questionsImage = questionsImage;
        this.questionsProfileName = questionsProfileName;
        this.questionsTime = questionsTime;
        this.commentProfileImage = commentProfileImage;
        this.userQuestionComments = userQuestionComments;
        this.questionsCompany = questionsCompany;
    }
    public Questions(String questions, String questionsIndustry, String questionsImage, String questionsProfileName, String questionsTime, String commentProfileImage, String userQuestionComments) {
        this.questions = questions;
        this.questionsIndustry = questionsIndustry;
        this.questionsImage = questionsImage;
        this.questionsProfileName = questionsProfileName;
        this.questionsTime = questionsTime;
        this.commentProfileImage = commentProfileImage;
        this.userQuestionComments=userQuestionComments;
    }

    public Questions(String commentUserImage, String commentUserMessage) {
        this.commentUserImage = commentUserImage;
        this.commentUserMessage = commentUserMessage;
    }


    public String getCommentUserImage() {
        return commentUserImage;
    }

    public String getCommentUserMessage() {
        return commentUserMessage;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionUserId() {
        return questionUserId;
    }
    public String getQuestions() {
        return questions;
    }

    public String getQuestionsIndustry() {
        return questionsIndustry;
    }

    public String getQuestionsImage() {
        return questionsImage;
    }

    public String getQuestionsProfileName() {
        return questionsProfileName;
    }

    public String getQuestionsTime() {
        return questionsTime;
    }

    public String getCommentProfileImage() {
        return commentProfileImage;
    }
    public String getUserQuestionComments(){
        return userQuestionComments;
    }
}

