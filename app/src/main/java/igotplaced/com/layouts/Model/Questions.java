package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/30/2017.
 */

public class Questions {
    private String questions;
    private String questionsIndustry;
    private String questionsImage;
    private String questionsProfileName;
    private String questionsTime;
    private String commentProfileImage;

    public Questions() {
    }

    public Questions(String questions, String questionsIndustry, String questionsImage, String questionsProfileName, String questionsTime, String commentProfileImage) {
        this.questions = questions;
        this.questionsIndustry = questionsIndustry;
        this.questionsImage = questionsImage;
        this.questionsProfileName = questionsProfileName;
        this.questionsTime = questionsTime;
        this.commentProfileImage = commentProfileImage;
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
}
