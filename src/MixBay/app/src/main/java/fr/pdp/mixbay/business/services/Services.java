package fr.pdp.mixbay.business.services;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.dataAccess.RepositoryI;
import fr.pdp.mixbay.business.exceptions.SessionManagementException;
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
        return repository.getSession().finish();
    }

    // TODO Change function name
    public static void requestMainUser() throws ExecutionException, InterruptedException {
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
        repository.getSession().previousMusic();
    }

    public static void playMusic() {
        repository.getSession().playMusic();
    }

    public static void nextMusic() {
        repository.getSession().nextMusic();
    }

    public static LocalPlaylist mix() {
        LocalPlaylist playlist = repository.getSession().generatePlaylist();
        repository.getSession().launchPlaylist();
        return playlist;
    }

    public static void CreateLogFile() {
        repository.getSession().createLogFile();
    }

    public static void addUserWithId(String id) throws ExecutionException, InterruptedException, SessionManagementException {
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

    public static User getCurrentUser() {
        return repository.getSession().getCurrentUser();
    }

    public static void syncCurrentTrack(String id) {
        getSession().syncCurrentTrack(id);

    }
}
