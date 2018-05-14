package mx.iteso.petgo.beans;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Solicitud implements Parcelable {
    private Uri mImageResource;
    private Uri mPetResource;
    private String clientName = "";
    private String walkDate = "";
    private String walkTime = "";
    private String clientAddress = "";
    private String tokenId = "";
    private User user;

    public Solicitud() {
    }

    public Solicitud(Uri mImageResource, Uri mPetResource, String clientName, String walkDate, String walkTime, String clientAddress, String tokenId) {
        this.mImageResource = mImageResource;
        this.mPetResource = mPetResource;
        this.clientName = clientName;
        this.walkDate = walkDate;
        this.walkTime = walkTime;
        this.clientAddress = clientAddress;
        this.tokenId = tokenId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Uri getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(Uri mImageResource) {
        this.mImageResource = mImageResource;
    }

    public Uri getmPetResource() {
        return mPetResource;
    }

    public void setmPetResource(Uri mPetResource) {
        this.mPetResource = mPetResource;
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

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mImageResource, flags);
        dest.writeParcelable(this.mPetResource, flags);
        dest.writeString(this.clientName);
        dest.writeString(this.walkDate);
        dest.writeString(this.walkTime);
        dest.writeString(this.clientAddress);
        dest.writeString(this.tokenId);
        dest.writeParcelable(this.user, flags);
    }

    protected Solicitud(Parcel in) {
        this.mImageResource = in.readParcelable(Uri.class.getClassLoader());
        this.mPetResource = in.readParcelable(Uri.class.getClassLoader());
        this.clientName = in.readString();
        this.walkDate = in.readString();
        this.walkTime = in.readString();
        this.clientAddress = in.readString();
        this.tokenId = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Solicitud> CREATOR = new Creator<Solicitud>() {
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
