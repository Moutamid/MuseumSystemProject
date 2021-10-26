package dev.moutamid.museumsystemproject.models;

import java.util.ArrayList;

public class BusinessDetailsModel {
    public String name, priceOfTicket, address, description, terms, imageUrl, uid,
            category, email, website, whatsapp, lastUpdateDate;
    public int categoryPosition, totalRatingCount;
    public ArrayList<String> catalogues;
    public double latitude, longitude;
    public float averageRating;

    public BusinessDetailsModel(String name, String priceOfTicket, String address, String description, String terms, String imageUrl, String uid, String category, String email, String website, String whatsapp, String lastUpdateDate, int categoryPosition, int totalRatingCount, ArrayList<String> catalogues, double latitude, double longitude, float averageRating) {
        this.name = name;
        this.priceOfTicket = priceOfTicket;
        this.address = address;
        this.description = description;
        this.terms = terms;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.category = category;
        this.email = email;
        this.website = website;
        this.whatsapp = whatsapp;
        this.lastUpdateDate = lastUpdateDate;
        this.categoryPosition = categoryPosition;
        this.totalRatingCount = totalRatingCount;
        this.catalogues = catalogues;
        this.latitude = latitude;
        this.longitude = longitude;
        this.averageRating = averageRating;
    }

    public int getTotalRatingCount() {
        return totalRatingCount;
    }

    public void setTotalRatingCount(int totalRatingCount) {
        this.totalRatingCount = totalRatingCount;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
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

    public ArrayList<String> getCatalogues() {
        return catalogues;
    }

    public void setCatalogues(ArrayList<String> catalogues) {
        this.catalogues = catalogues;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryPosition() {
        return categoryPosition;
    }

    public void setCategoryPosition(int categoryPosition) {
        this.categoryPosition = categoryPosition;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceOfTicket() {
        return priceOfTicket;
    }

    public void setPriceOfTicket(String priceOfTicket) {
        this.priceOfTicket = priceOfTicket;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public BusinessDetailsModel() {
    }
}
