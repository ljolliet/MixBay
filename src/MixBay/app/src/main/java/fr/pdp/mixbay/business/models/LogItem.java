/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.models;

import java.util.Date;

public class LogItem {

    public final Date date;
    public final String username;
    public final LogAction action;
    public final String details;
    public final String trackName;
    public final String algorithm;

    public enum LogAction {
        NEXT,
        PREVIOUS,
        PLAY,
        PAUSE,
        LIKE,
        UNLIKE
    }

    /**
     * Log information (corresponding to a user interaction) that will be
     * printed in the log file.
     *
     * @param username  User that has performed the action.
     * @param action    Action performed.
     * @param details   Details about the action performed.
     * @param trackName Track corresponding to the action.
     * @param algorithm Algorithm that has generated the locale playlist.
     */
    public LogItem(String username, LogAction action, String details,
                   String trackName, String algorithm) {
        this.username = username;
        this.action = action;
        this.details = details;
        this.trackName = trackName;
        this.algorithm = algorithm;
        this.date = new Date();
    }
}
