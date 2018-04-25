package mx.iteso.petgo.beans;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Solicitud implements Parcelable {
    private Uri mImageResource;
    private String clientName = "";
    private String walkDate = "";
    private String walkTime = "";
    private String clientAddress = "";

    public Solicitud() {
    }

    public Solicitud(String mImageResource, String clientName, String walkDate, String walkTime, String clientAddress) {
        this.mImageResource = Uri.parse(mImageResource);
        this.clientName = clientName;
        this.walkDate = walkDate;
        this.walkTime = walkTime;
        this.clientAddress = clientAddress;
    }
    
    public Uri getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(Uri mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getWalkDate() {
        return walkDate;
    }

    public void setWalkDate(String walkDate) {
        this.walkDate = walkDate;
    }

    public String getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(String walkTime) {
        this.walkTime = walkTime;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mImageResource.toString());
        dest.writeString(this.clientName);
        dest.writeString(this.walkDate);
        dest.writeString(this.walkTime);
        dest.writeString(this.clientAddress);
    }

    protected Solicitud(Parcel in) {
        this.mImageResource = Uri.parse(in.readString());
        this.clientName = in.readString();
        this.walkDate = in.readString();
        this.walkTime = in.readString();
        this.clientAddress = in.readString();
    }

    public static final Parcelable.Creator<Solicitud> CREATOR = new Parcelable.Creator<Solicitud>() {
        @Override
        public Solicitud createFromParcel(Parcel source) {
            return new Solicitud(source);
        }

        @Override
        public Solicitud[] newArray(int size) {
            return new Solicitud[size];
        }
    };
}
