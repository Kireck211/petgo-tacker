package mx.iteso.petgo.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fcamacho on 4/3/18.
 */

public class User implements Parcelable {
    private String email;
    private String provider;
    private String name;
    private String tokenId;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeString(this.email);
        dest.writeString(this.provider);
        dest.writeString(this.name);
        dest.writeString(this.tokenId);
        dest.writeString(this.imageUrl);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.email = in.readString();
        this.provider = in.readString();
        this.name = in.readString();
        this.tokenId = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
