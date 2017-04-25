package com.task.vasskob.googlemapsrealm.util;


import com.task.vasskob.googlemapsrealm.R;

public class ManageMarkerIcon {
    // TODO: 25/04/17 is better to create

    public static int manageMarkerIcon(String markerIcon) {
        switch (markerIcon) {
            case "ic_icon1":
                return R.drawable.ic_icon1;
            case "ic_icon2":
                return R.drawable.ic_icon2;
            case "ic_icon3":
                return R.drawable.ic_icon3;
            case "ic_icon4":
                return R.drawable.ic_icon4;
            default:
                return R.drawable.ic_default_marker;
        }
    }

    public static String manageReverseMarkerIcon(int id) {
        switch (id) {
            case R.id.icon1:
                return "ic_icon1";
            case R.id.icon2:
                return "ic_icon2";
            case R.id.icon3:
                return "ic_icon3";
            case R.id.icon4:
                return "ic_icon4";
            default:
                return "ic_default_marker";
        }
    }

}

