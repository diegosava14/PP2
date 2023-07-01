package com.example.giftgeek.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Wishlist implements Parcelable {
    private String name;
    private String description;
    private String endDate;
    private int userId;
    private int wishlistId;
    private String creationDate;
    private List<Gift> gifts;

    public Wishlist(String name, String description, String endDate, int userId, int wishlistId, String creationDate, List<Gift> gifts) {
        this.name = name;
        this.description = description;
        this.endDate = endDate;
        this.userId = userId;
        this.wishlistId = wishlistId;
        this.creationDate = creationDate;
        this.gifts = gifts;

    }
    protected Wishlist(Parcel in) {
        name = in.readString();
        description = in.readString();
        endDate = in.readString();
        userId = in.readInt();
        wishlistId = in.readInt();
        creationDate = in.readString();
    }

    public static final Creator<Wishlist> CREATOR = new Creator<Wishlist>() {
        @Override
        public Wishlist createFromParcel(Parcel in) {
            return new Wishlist(in);
        }

        @Override
        public Wishlist[] newArray(int size) {
            return new Wishlist[size];
        }
    };

    public List<Gift> getGifts() {
        return gifts;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getUserId() {
        return userId;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setName(String title) {
        this.name = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(description);
            dest.writeString(endDate);
            dest.writeInt(userId);
            dest.writeInt(wishlistId);
            dest.writeString(creationDate);
    }
}
