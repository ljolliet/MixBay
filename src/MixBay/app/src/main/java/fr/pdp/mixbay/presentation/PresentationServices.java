package fr.pdp.mixbay.presentation;

import android.app.Activity;

public class PresentationServices {

        private static MainActivity activity;

        private PresentationServices() {} // Private constructor

        public static void setActivity(MainActivity a){
            activity = a;
        }

    public static void updateCover() {
        activity.updateCover();
    }
}
