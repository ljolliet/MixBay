/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.services;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.dataAccess.RepositoryI;
import fr.pdp.mixbay.business.exceptions.PlayerException;
import fr.pdp.mixbay.business.exceptions.SessionManagementException;
import fr.pdp.mixbay.business.models.AlgoI;
import fr.pdp.mixbay.business.models.LocalPlaylist;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Session;
import fr.pdp.mixbay.business.models.User;
import fr.pdp.mixbay.data.InMemoryRepository;
import fr.pdp.mixbay.data.SpotifyAPI;

public class Services {

    private static RepositoryI repository = new InMemoryRepository();

    private Services() {} // Private constructor

    public static Session getSession() {
        return repository.getSession();
    }

    public static void setSession(Session session) {
        repository.setSession(session);
    }

    public static Session createSpotifySession() {
        APIManagerI api = new SpotifyAPI();
        Session session = new Session(api);

        setSession(session);

        return session;
    }

    public static boolean disconnectSession() {
        return getSession().finish();
    }

    public static void requestMainUser() throws ExecutionException,
            InterruptedException {
        APIManagerI api = getSession().getApi();
        // Get User
        User user = api.getMainUser().get();
        System.out.println(user.id);
        // Get user's playlists
        Set<Playlist> playlists = api.getUserPlaylists(user.id).get();
        System.out.println("size = " + playlists.size());
        user.addAllPlaylists(playlists); // Add them to the User

        getSession().addUser(user);
    }

    public static void previousMusic() {
        getSession().previousMusic();
    }

    public static void playMusic() {
        getSession().playMusic();
    }

    public static void pauseMusic() {
        getSession().pauseMusic();
    }

    public static void nextMusic() {
        getSession().nextMusic();
    }

    public static LocalPlaylist mix() throws PlayerException {
        LocalPlaylist playlist = getSession().generatePlaylist();
        getSession().launchPlaylist();
        return playlist;
    }

    public static void CreateLogFile() {
        getSession().createLogFile();
    }

    public static void addUserWithId(String id) throws ExecutionException,
            InterruptedException, SessionManagementException {
        APIManagerI api = getSession().getApi();

        // Test if the user is already added
        getSession().testIfUserExists(id);

        // Request for the user
        User user = api.getUser(id).get();
        // Get user's playlists
        Set<Playlist> playlists = api.getUserPlaylists(id).get();
        System.out.println("size = " + playlists.size());
        user.addAllPlaylists(playlists); // Add them to the User

        getSession().addUser(user);
    }

    public static void removeUser(User user) {
        getSession().getUsers().remove(user);
    }

    public static List<User> getUsers() {
        return new ArrayList<>(getSession().getUsers());
    }

    public static User getCurrentUser() {
        return getSession().getCurrentUser();
    }

    public static void setCurrentUser(User user) {
        getSession().setCurrentUser(user);
    }

    public static void syncCurrentTrack(String id) {
        getSession().syncCurrentTrack(id);
    }

    public static boolean isCurrentTrackLiked() {
        return getSession().getCurrentTrack().isLiked();
    }

    public static void likeMusic() {
        getSession().likeCurrentTrack();
    }

    public static void unlikeMusic() {
        getSession().unlikeCurrentTrack();
    }

    public static boolean isMixed() {
        return getSession().isMixed();
    }

    public static void setAlgorithm(AlgoI algorithm) {
        getSession().setAlgo(algorithm);
    }

    public static String getAlgorithmName(Context context) {
        return getSession().getAlgo().getName(context);
    }

    public static boolean isCurrentUser(User user) {
        return getCurrentUser().equals(user);
    }

    public static boolean isCurrentTrackPaused() {
        return getSession().isPaused();
    }

    public static void onResumeApi() {
        getSession().getApi().onResume();
    }

    public static void onPauseApi() {
        getSession().getApi().onPause();
    }
}
