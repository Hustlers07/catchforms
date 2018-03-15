package com.catchforms.vinod.rupesh;

/**
 * Created by root on 5/28/17.
 */

public class personinfo {
   static String personName ;
    static String personEmail ;
    static String personId ;
    static String loggedinwith;

    public static String getLoggedinwith() {
        return loggedinwith;
    }

    public static void setLoggedinwith(String loggedinwith) {
        personinfo.loggedinwith = loggedinwith;
    }

    public static String getPersonName() {
        return personName;
    }

    public static void setPersonName(String personName) {
        personinfo.personName = personName;
    }

    public static String getPersonEmail() {
        return personEmail;
    }

    public static void setPersonEmail(String personEmail) {
        personinfo.personEmail = personEmail;
    }

    public static String getPersonId() {
        return personId;
    }

    public static void setPersonId(String personId) {
        personinfo.personId = personId;
    }
}
