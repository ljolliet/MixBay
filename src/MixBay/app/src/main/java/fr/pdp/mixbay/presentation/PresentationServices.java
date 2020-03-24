/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.presentation;

public class PresentationServices {

    private static MainActivity activity;

    private PresentationServices() {
    } // Private constructor

    public static void setActivity(MainActivity a) {
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
