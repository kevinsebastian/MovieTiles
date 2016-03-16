package com.example.android.movietiles.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieInfo implements Parcelable {

    private String title;
    private String imageLink;
    private String synopsis;
    private String rating;
    private String releaseDate;

    // Empty Constructor
    public MovieInfo() {

    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = "http://image.tmdb.org/t/p/w185/" + imageLink;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    // Parcelling Constructor
    public MovieInfo(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.title = data[0];
        this.imageLink = data[1];
        this.synopsis = data[2];
        this.rating = data[3];
        this.releaseDate = data[4];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.title,
                this.imageLink,
                this.synopsis,
                this.rating,
                this.releaseDate});
    }

    // Parcelable Creator
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
