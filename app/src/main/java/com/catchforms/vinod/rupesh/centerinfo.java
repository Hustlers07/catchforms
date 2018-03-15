package com.catchforms.vinod.rupesh;

/**
 * Created by root on 5/28/17.
 */

public class centerinfo {
    static String centerName;
    static String address;
    static boolean aadhar;
    static boolean voterid;
    static boolean pancard;
    static String restother;
    static String contact;
    static String password;
    static String country;
    static String state;


    public static String getCenterName() {
        return centerName;
    }

    public static void setCenterName(String centerName) {
        centerinfo.centerName = centerName;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        centerinfo.address = address;
    }

    public static boolean isAadhar() {
        return aadhar;
    }

    public static void setAadhar(boolean aadhar) {
        centerinfo.aadhar = aadhar;
    }

    public static boolean isVoterid() {
        return voterid;
    }

    public static void setVoterid(boolean voterid) {
        centerinfo.voterid = voterid;
    }

    public static boolean isPancard() {
        return pancard;
    }

    public static void setPancard(boolean pancard) {
        centerinfo.pancard = pancard;
    }

    public static String getRestother() {
        return restother;
    }

    public static void setRestother(String restother) {
        centerinfo.restother = restother;
    }

    public static String getContact() {
        return contact;
    }

    public static void setContact(String contact) {
        centerinfo.contact = contact;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        centerinfo.password = password;
    }

    public static String getCountry() {
        return country;
    }

    public static void setCountry(String country) {
        centerinfo.country = country;
    }

    public static String getState() {
        return state;
    }

    public static void setState(String state) {
        centerinfo.state = state;
    }
}
