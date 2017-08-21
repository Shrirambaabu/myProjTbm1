package igotplaced.com.layouts.Model;

/**
 * Created by Shriram on 21-Aug-17.
 */

public class SearchResultsModel {

    private String postId;
    private String postedUserId;
    private String postUserName;
    private String postUserImage;
    private String postDate;

    public SearchResultsModel(String postId, String postedUserId, String postUserName, String postUserImage, String postDate, String postMessage, String postIndustry, String postCompany) {
        this.postId = postId;
        this.postedUserId = postedUserId;
        this.postUserName = postUserName;
        this.postUserImage = postUserImage;
        this.postDate = postDate;
        this.postMessage = postMessage;
        this.postIndustry = postIndustry;
        this.postCompany = postCompany;
    }

    private String postMessage;
    private String postIndustry;
    private String postCompany;

    private String interviewId;
    private String inteviewUserid;
    private String interviewProfileName;
    private String interviewProfileImage;
    private String interviewProfileDate;
    private String interviewMessage;
    private String interviewIndustry;
    private String interviewCompany;

    private String eventId;
    private String eventUserId;
    private String eventUserName;
    private String eventUserImage;
    private String eventDate;
    private String eventName;
    private String eventType;

    public SearchResultsModel(String eventId, String eventUserId, String eventUserName, String eventUserImage, String eventDate, String eventName, String eventType, String eventLocation, String dateTime, String count, String event, String notes, String eventIndustry, String eventCompany) {
        this.eventId = eventId;
        this.eventUserId = eventUserId;
        this.eventUserName = eventUserName;
        this.eventUserImage = eventUserImage;
        this.eventDate = eventDate;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventLocation = eventLocation;
        this.dateTime = dateTime;
        this.count = count;
        this.event = event;
        this.notes = notes;
        this.eventIndustry = eventIndustry;
        this.eventCompany = eventCompany;
    }

    private String eventLocation;
    private String dateTime;
    private String count;
    private String event;
    private String notes;
    private String eventIndustry;
    private String eventCompany;


    private String questionId;
    private String questionUserId;
    private String questionUserName;
    private String questionUserImage;
    private String questionDate;
    private String questionMessage;
    private String questionIndustry;
    private String questionCompany;

    public String getPostId() {
        return postId;
    }

    public String getPostedUserId() {
        return postedUserId;
    }

    public String getPostUserName() {
        return postUserName;
    }

    public String getPostUserImage() {
        return postUserImage;
    }

    public String getPostDate() {
        return postDate;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public String getPostIndustry() {
        return postIndustry;
    }

    public String getPostCompany() {
        return postCompany;
    }

    public String getInterviewId() {
        return interviewId;
    }

    public String getInteviewUserid() {
        return inteviewUserid;
    }

    public String getInterviewProfileName() {
        return interviewProfileName;
    }

    public String getInterviewProfileImage() {
        return interviewProfileImage;
    }

    public String getInterviewProfileDate() {
        return interviewProfileDate;
    }

    public String getInterviewMessage() {
        return interviewMessage;
    }

    public String getInterviewIndustry() {
        return interviewIndustry;
    }

    public String getInterviewCompany() {
        return interviewCompany;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventUserId() {
        return eventUserId;
    }

    public String getEventUserName() {
        return eventUserName;
    }

    public String getEventUserImage() {
        return eventUserImage;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getCount() {
        return count;
    }

    public String getEvent() {
        return event;
    }

    public String getNotes() {
        return notes;
    }

    public String getEventIndustry() {
        return eventIndustry;
    }

    public String getEventCompany() {
        return eventCompany;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionUserId() {
        return questionUserId;
    }

    public String getQuestionUserName() {
        return questionUserName;
    }

    public String getQuestionUserImage() {
        return questionUserImage;
    }

    public String getQuestionDate() {
        return questionDate;
    }

    public String getQuestionMessage() {
        return questionMessage;
    }

    public String getQuestionIndustry() {
        return questionIndustry;
    }

    public String getQuestionCompany() {
        return questionCompany;
    }

    public SearchResultsModel(String postId, String postedUserId, String postUserName, String postUserImage, String postDate, String postMessage, String postIndustry, String postCompany, String interviewId, String inteviewUserid, String interviewProfileName, String interviewProfileImage, String interviewProfileDate, String interviewMessage, String interviewIndustry, String interviewCompany, String eventId, String eventUserId, String eventUserName, String eventUserImage, String eventDate, String eventName, String eventType, String eventLocation, String dateTime, String count, String event, String notes, String eventIndustry, String eventCompany, String questionId, String questionUserId, String questionUserName, String questionUserImage, String questionDate, String questionMessage, String questionIndustry, String questionCompany) {
        this.postId = postId;
        this.postedUserId = postedUserId;
        this.postUserName = postUserName;
        this.postUserImage = postUserImage;
        this.postDate = postDate;
        this.postMessage = postMessage;
        this.postIndustry = postIndustry;
        this.postCompany = postCompany;
        this.interviewId = interviewId;
        this.inteviewUserid = inteviewUserid;
        this.interviewProfileName = interviewProfileName;
        this.interviewProfileImage = interviewProfileImage;
        this.interviewProfileDate = interviewProfileDate;
        this.interviewMessage = interviewMessage;
        this.interviewIndustry = interviewIndustry;
        this.interviewCompany = interviewCompany;
        this.eventId = eventId;
        this.eventUserId = eventUserId;
        this.eventUserName = eventUserName;
        this.eventUserImage = eventUserImage;
        this.eventDate = eventDate;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventLocation = eventLocation;
        this.dateTime = dateTime;
        this.count = count;
        this.event = event;
        this.notes = notes;
        this.eventIndustry = eventIndustry;
        this.eventCompany = eventCompany;
        this.questionId = questionId;
        this.questionUserId = questionUserId;
        this.questionUserName = questionUserName;
        this.questionUserImage = questionUserImage;
        this.questionDate = questionDate;
        this.questionMessage = questionMessage;
        this.questionIndustry = questionIndustry;
        this.questionCompany = questionCompany;
    }

    public SearchResultsModel() {
    }
}
