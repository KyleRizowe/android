package com.fuel4media.carrythistoo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AdminMessage implements Parcelable{
    private String title;
    private String message;



    public AdminMessage(){}

    protected AdminMessage(Parcel in) {
        title = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdminMessage> CREATOR = new Creator<AdminMessage>() {
        @Override
        public AdminMessage createFromParcel(Parcel in) {
            return new AdminMessage(in);
        }

        @Override
        public AdminMessage[] newArray(int size) {
            return new AdminMessage[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
