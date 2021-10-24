package dev.moutamid.museumsystemproject.models;

public class BusinessDetailsModel {
    public String name, priceOfTicket, address, userManual, terms, pushKey, imageUrl, uid;

    public BusinessDetailsModel(String name, String priceOfTicket, String address, String userManual, String terms, String pushKey, String imageUrl, String uid) {
        this.name = name;
        this.priceOfTicket = priceOfTicket;
        this.address = address;
        this.userManual = userManual;
        this.terms = terms;
        this.pushKey = pushKey;
        this.imageUrl = imageUrl;
        this.uid = uid;
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

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
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

    public String getUserManual() {
        return userManual;
    }

    public void setUserManual(String userManual) {
        this.userManual = userManual;
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
