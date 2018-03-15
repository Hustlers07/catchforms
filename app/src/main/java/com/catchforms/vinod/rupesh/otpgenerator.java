package com.catchforms.vinod.rupesh;

/**
 * Created by root on 6/20/17.
 */

public class otpgenerator {
    public static Integer random(){
        Integer integer= (int)(Math.random()*9000)+1000;
        return integer;
    }
}
