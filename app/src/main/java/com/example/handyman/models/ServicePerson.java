package com.example.handyman.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServicePerson extends BaseObservable {

    public float rating;
    public String userId;
    public String name, email;
    public String reason;
    public double latitude, longitude;
    public String occupation;
    public String response;
    public String location;
    public String date;
    public String about, number, accountType;
    public String image;
    public String distanceBetween;
    public String senderPhoto;
    public String senderName;
    public String handyManName, handyManPhoto;



    public ServicePerson() {
    }

    public ServicePerson(String userId, String name, String email, String accountType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.accountType = accountType;
    }

    @BindingAdapter("imageUrl")
    public static void loadImages(CircleImageView imageView, String imageUrl) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Bindable
    public float getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Bindable
    public String getReason() {
        return reason;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Bindable
    public String getLocation() {
        return location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Bindable
    public String getAbout() {
        return about;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Bindable
    public String getNumber() {
        return number;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Bindable
    public String getAccountType() {
        return accountType;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDistanceBetween() {
        return distanceBetween;
    }

    public void setDistanceBetween(String distanceBetween) {
        this.distanceBetween = distanceBetween;
    }

    public String getSenderPhoto() {
        return senderPhoto;
    }

    public void setSenderPhoto(String senderPhoto) {
        this.senderPhoto = senderPhoto;
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Bindable
    public String getSenderName() {
        return senderName;
    }

    public void setHandyManName(String handyManName) {
        this.handyManName = handyManName;
    }

    public String getHandyManPhoto() {
        return handyManPhoto;
    }

    public void setHandyManPhoto(String handyManPhoto) {
        this.handyManPhoto = handyManPhoto;
    }

    @Bindable
    public String getHandyManName() {
        return handyManName;
    }
}
