package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/30/2017.
 */

public class Events {

    private String eventCommentImage;
    private String eventCommentMessage;
    private String eventId;
    private String eventUserId;
    private String eventCaption;
    private String eventDesignation;
    private String eventVenue;
    private String eventDate;
    private String eventRegistered;
    private String eventStatus;
    private String event;
    private String eventIndustry;
    private String eventImage;
    private String eventProfileName;
    private String eventTime;
    private String commentProfileImage;
    private String userComment;

    public Events(String eventCommentImage, String eventCommentMessage) {
        this.eventCommentImage = eventCommentImage;
        this.eventCommentMessage = eventCommentMessage;
    }

    public Events(String eventId, String eventUserId, String eventCaption, String eventDesignation, String eventVenue, String eventDate, String eventRegistered, String eventStatus, String event, String eventIndustry, String eventImage, String eventProfileName, String eventTime, String commentProfileImage, String userComment) {
        this.eventId = eventId;
        this.eventUserId = eventUserId;
        this.eventCaption = eventCaption;
        this.eventDesignation = eventDesignation;
        this.eventVenue = eventVenue;
        this.eventDate = eventDate;
        this.eventRegistered = eventRegistered;
        this.eventStatus = eventStatus;
        this.event = event;
        this.eventIndustry = eventIndustry;
        this.eventImage = eventImage;
        this.eventProfileName = eventProfileName;
        this.eventTime = eventTime;
        this.commentProfileImage = commentProfileImage;
        this.userComment = userComment;
    }

    public Events(String eventCaption, String eventDesignation, String eventVenue, String eventDate, String eventRegistered, String eventStatus, String event, String eventIndustry, String eventImage, String eventProfileName, String eventTime, String commentProfileImage, String userComment) {
        this.eventCaption = eventCaption;
        this.eventDesignation = eventDesignation;
        this.eventVenue = eventVenue;
        this.eventDate = eventDate;
        this.eventRegistered = eventRegistered;
        this.eventStatus = eventStatus;
        this.event = event;
        this.eventIndustry = eventIndustry;
        this.eventImage = eventImage;
        this.eventProfileName = eventProfileName;
        this.eventTime = eventTime;
        this.commentProfileImage = commentProfileImage;
        this.userComment = userComment;
    }

    public Events(String eventCaption, String eventDesignation, String eventVenue, String eventDate, String eventRegistered, String eventIndustry, String eventImage, String eventProfileName, String eventTime, String commentProfileImage, String userComment) {
        this.eventCaption = eventCaption;
        this.eventDesignation = eventDesignation;
        this.eventVenue = eventVenue;
        this.eventDate = eventDate;
        this.eventRegistered = eventRegistered;
        this.eventIndustry = eventIndustry;
        this.eventImage = eventImage;
        this.eventProfileName = eventProfileName;
        this.eventTime = eventTime;
        this.commentProfileImage = commentProfileImage;
        this.userComment = userComment;
    }

    public String getEventCommentImage() {
        return eventCommentImage;
    }

    public String getEventCommentMessage() {
        return eventCommentMessage;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventUserId() {
        return eventUserId;
    }

    public String getEventCaption() {
        return eventCaption;
    }

    public String getEventDesignation() {
        return eventDesignation;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventRegistered() {
        return eventRegistered;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public String getEvent() {
        return event;
    }

    public String getEventIndustry() {
        return eventIndustry;
    }

    public String getEventImage() {
        return eventImage;
    }

    public String getEventProfileName() {
        return eventProfileName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getCommentProfileImage() {
        return commentProfileImage;
    }

    public String getUserComment() {
        return userComment;
    }

    public Events() {

    }
}
