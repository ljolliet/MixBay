package fr.pdp.mixbay.presentation;

public class PresentationServices {

        private static MainActivity activity;

        private PresentationServices() {} // Private constructor

        public static void setActivity(MainActivity a){
            activity = a;
        }

    public static void updateCover() {
        activity.updateCover();
    }

    public static void updateLikeButton() {
        activity.updateLikeButton();
    }

    public static void callForWriteRights() {
            activity.getWriteRights();
    }
}
