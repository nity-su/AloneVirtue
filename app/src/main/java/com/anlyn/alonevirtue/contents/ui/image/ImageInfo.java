package com.anlyn.alonevirtue.contents.ui.image;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageInfo implements Parcelable {

    private String url;

    public ImageInfo(String name, String url) {
        this.url = url;
    }
    public ImageInfo(Parcel in) {
        this.url = in.readString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            // TODO Auto-generated method stub
            return new ImageInfo[size];
        }

    };



}
