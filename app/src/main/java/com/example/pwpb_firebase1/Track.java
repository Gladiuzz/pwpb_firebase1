package com.example.pwpb_firebase1;

public class Track {
    private String trackID;
    private String trackName;
    private int trackRating;

    public Track(){
        //need empty method
    }

    public Track(String trackID, String trackName, int trackRating) {
        this.trackID = trackID;
        this.trackName = trackName;
        this.trackRating = trackRating;
    }

    public String getTrackID() {
        return trackID;
    }

    public String getTrackName() {
        return trackName;
    }

    public int getTrackRating() {
        return trackRating;
    }
}
