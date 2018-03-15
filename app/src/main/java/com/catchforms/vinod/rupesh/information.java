package com.catchforms.vinod.rupesh;

/**
 * Created by root on 4/2/17.
 */

public class information {
    static String data;
    static String title;
    static String functionname;
    public static void aadhar(int i) {

        functionname="aadhardata";
        switch (i) {
            case 0: {

                title="Apply";
                data="apply";
                break;
            }
            case 1: {
                title="Documents";
                data="documents";
                break;
            }

            case 2: {
                title="Duplicate";
                data="duplicate";
                break;
            }
            case 3: {
                title="Correction";
                data="correction";
                break;
            }
            case 4: {
                title="Track";
                data="track";
                break;
            }
        }
        //return null;
    }

    public static void voterid(int i) {
        functionname="voteriddata";
        switch (i) {
            case 0: {

                title="Apply";
                data="apply";
                break;
            }
            case 1: {
                title="Documents";
                data="documents";
                break;
            }
            case 2: {
                title="Eligibility";
                data = "eligibility";
                break;
            }
            case 3: {
                title="Lost";
                data="lost";
                break;
            }
            case 4: {
                title="Correction";
                data="correction";
                break;
            }
            case 5: {
                title="Check List";
                data="checklist";
                break;
            }
        }
        //return null;
    }


    public static void pan(int i) {
        functionname="pandata";
        switch (i) {
            case 0: {
                title="Apply";
                data="apply";

                break;
            }
            case 1: {
                title="Documents";
                data="documents";
                break;
            }
            case 2: {
                title="Duplicate";
                data="duplicate";
                break;
            }
            case 3: {
                title="Correction";
                data="correction";
                break;
            }
        }
        //return null;
    }

    public static void ration(int i) {
        functionname="rationdata";
        switch (i) {
            case 0: {
                title="Apply";
                data="apply";

                break;
            }
            case 1: {
                title="Documents";
                data="documents";
                break;
            }
            case 2: {
                title="Duplicate";
                data="duplicate";
                break;
            }
            case 3: {
                title="Track Status";
                data="track";
                break;
            }
            case 4: {
                title="Add Member";
                data="addmember";
                break;
            }
        }
        //return null;
    }
    public static void licence(int i){
        functionname="licencedata";
        switch(i){
            case 0:{
                title="Apply";
                data="apply";
                break;
            }
            case 1:{
                title="Documents";
                data="document";
                break;
            }
            case 2:{
                title="Duplicate";
                data="duplicate";
                break;
            }
            case 3:{
                title="Renew";
                data="renew";
                break;
            }
        }
    }
}