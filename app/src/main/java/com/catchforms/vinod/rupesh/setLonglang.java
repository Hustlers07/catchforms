package com.catchforms.vinod.rupesh;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Rupesh gupta on 4/1/2017.
 */

public class setLonglang {

    public static LatLng getCurrent() {
        return current;
    }

    public static void setCurrent(LatLng current) {
        setLonglang.current = current;
    }

    static LatLng current;


}
