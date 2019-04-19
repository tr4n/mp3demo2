package com.example.mp3demo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Audio implements Parcelable {

    public static final Creator<Audio> CREATOR = new Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel source) {
            return new Audio(source);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };

    private final String mTitle;
    private final String mArtist;
    private final String mPath;

    public Audio(String title, String artist, String data) {
        mTitle = title;
        mArtist = artist;
        mPath = data;
    }

    private Audio(Parcel in) {
        mTitle = in.readString();
        mArtist = in.readString();
        mPath = in.readString();
    }

    @Override
    public String toString() {
        return "{title= " + mTitle + ", path= " + mPath + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mArtist);
        dest.writeString(mPath);
    }

    public String getTitle() {
        return mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getPath() {
        return mPath;
    }

}
