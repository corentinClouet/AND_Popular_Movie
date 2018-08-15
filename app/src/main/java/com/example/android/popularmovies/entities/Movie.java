package com.example.android.popularmovies.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "favorite")
public class Movie implements Parcelable {

    @PrimaryKey
    private int movieId;
    private String title;
    private String releaseDate;
    private String posterUrl;
    private String wallpaperUrl;
    private double voteAverage;
    private String synopsis;

    public Movie(int movieId, String title, String releaseDate, String posterUrl, String wallpaperUrl, double voteAverage, String synopsis) {
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.wallpaperUrl = wallpaperUrl;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
    }

    @Ignore
    public Movie() {
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

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

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getWallpaperUrl() {
        return wallpaperUrl;
    }

    public void setWallpaperUrl(String wallpaperUrl) {
        this.wallpaperUrl = wallpaperUrl;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.movieId);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterUrl);
        dest.writeString(this.wallpaperUrl);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.synopsis);
    }

    protected Movie(Parcel in) {
        this.movieId = in.readInt();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.posterUrl = in.readString();
        this.wallpaperUrl = in.readString();
        this.voteAverage = in.readDouble();
        this.synopsis = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
