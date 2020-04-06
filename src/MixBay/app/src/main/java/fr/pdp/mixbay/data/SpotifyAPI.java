/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.android.appremote.api.error.AuthenticationFailedException;
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.LoggedOutException;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.error.OfflineModeException;
import com.spotify.android.appremote.api.error.SpotifyConnectionTerminatedException;
import com.spotify.android.appremote.api.error.SpotifyDisconnectedException;
import com.spotify.android.appremote.api.error.UnsupportedFeatureVersionException;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.exceptions.APIConnectionException;
import fr.pdp.mixbay.business.exceptions.APIRequestException;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.TrackFeatures;
import fr.pdp.mixbay.business.models.User;
import fr.pdp.mixbay.business.services.Services;
import fr.pdp.mixbay.business.utils.Factory;
import fr.pdp.mixbay.presentation.PresentationServices;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class SpotifyAPI implements APIManagerI {

    private static final String CLIENT_ID = "cb64858e27964a13b85d3e7e5e19b1d3";
    private static final String REDIRECT_URI = "mixbay://callback";
    private static final String[] SCOPES = new String[]{"user-read-email",
            "user-read-private"};

    private Context context;

    private final OkHttpClient requestClient = new OkHttpClient();
    private String accessToken;

    private SpotifyAppRemote mSpotifyAppRemote;
    private ConnectionParams connectionParams =
            new ConnectionParams.Builder(CLIENT_ID)
                    .setRedirectUri(REDIRECT_URI)
                    .showAuthView(true)
                    .build();

    private Connector.ConnectionListener connectionListener =
            new Connector.ConnectionListener() {
                @Override
                public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote;
                    Log.d("SpotifyAPI", "Spotify remote Connected");
                    subscribeToPlayerChange();
                }

                @Override
                public void onFailure(Throwable error) {
                    Log.e("SpotifyAPI", error.getMessage(), error);

                    AlertDialog.Builder builder = new AlertDialog
                            .Builder(context, R.style.AlertDialogStyle);

                    builder.setTitle(R.string.error_title);
                    builder.setCancelable(false);

                    if (error instanceof CouldNotFindSpotifyApp) {
                        builder.setMessage(R.string.spotify_not_installed);
                    }
                    else if (error instanceof UnsupportedFeatureVersionException) {
                        builder.setMessage(R.string.spotify_not_updated);
                    }
                    else if (error instanceof AuthenticationFailedException) {
                        builder.setMessage(R.string.spotify_app_not_authorized);
                    }
                    else if (error instanceof NotLoggedInException ||
                            error instanceof LoggedOutException) {
                        builder.setMessage(R.string.spotify_not_connected);
                    }
                    else if (error instanceof OfflineModeException) {
                        builder.setMessage(R.string.spotify_offline);
                    }
                    else if (error instanceof SpotifyDisconnectedException ||
                            error instanceof SpotifyConnectionTerminatedException) {
                        builder.setMessage(R.string.spotify_closed);
                    }
                    else {
                        // error instanceof SpotifyRemoteServiceException
                        // error instanceof UserNotAuthorizedException
                        builder.setMessage(R.string.api_request_error);
                    }

                    builder.show();
                }

            };

    @Override
    public void connect(Context context) {
        this.context = context;

        // Web API connection
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID,
                        AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(SCOPES);
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity((Activity) context,
                SPOTIFY_REQUEST_CODE, request);
    }

    @Override
    public void onConnectionResult(int resultCode, Intent intent)
            throws APIConnectionException {
        AuthorizationResponse response = AuthorizationClient
                .getResponse(resultCode, intent);

        switch (response.getType()) {
            // Response was successful and contains auth token
            case TOKEN:
                Log.d("SpotifyAPI", "The token is: "
                        + response.getAccessToken());
                this.setAccessToken(response.getAccessToken());
                break;

            // Auth flow returned an error
            case ERROR:
                throw new APIConnectionException(response.getError());

            // Most likely auth flow was cancelled
            default:
                throw new APIConnectionException(context
                        .getString(R.string.api_request_error));
        }
    }

    @Override
    public boolean disconnect() {
        this.emptyQueue();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);

        if (context == null)
            return false;

        AuthorizationClient.clearCookies(context);
        return true;
    }

    @Override
    public void onResume() {
        // Connect Spotify remote only when app is in foreground
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        SpotifyAppRemote.connect(context, connectionParams, connectionListener);
    }

    @Override
    public void onPause() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    public Future<User> getUser(String id) {
        // Create User info request
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/" + id)
                .addHeader("Authorization","Bearer "
                        + this.accessToken)
                .build();

        Call call = this.requestClient.newCall(request);
        ExecutorService pool = Executors.newFixedThreadPool(1);

        // Return a Future
        return pool.submit(() -> {
            final JSONObject jsonObject = new JSONObject(call.execute()
                    .body().string());
            Log.d("SpotifyAPI", jsonObject.toString());

            // If user does not exist
            if (jsonObject.has("error"))
                throw new APIRequestException(context
                        .getString(R.string.invalid_user_id));

            // Return new User
            return Factory.getUser(id,
                    jsonObject.getString("display_name"));

        });
    }

    @Override
    public Future<User> getMainUser() {
        // Create main user info request
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization","Bearer "
                        + this.accessToken)
                .build();

        Call call = this.requestClient.newCall(request);
        ExecutorService pool = Executors.newFixedThreadPool(1);

        // Return a Future
        return pool.submit(() -> {
            final JSONObject jsonObject = new JSONObject(call.execute()
                    .body().string());

            // If user does not exist
            if (jsonObject.has("error"))
                throw new APIRequestException(context
                        .getString(R.string.api_request_error));

            String display_name = jsonObject.getString("display_name");
            String id = jsonObject.getString("id");

            // Return new User
            return Factory.getUser(id, display_name);
        });
    }

    @Override
    public Future<Set<Playlist>> getUserPlaylists(String userId) {
        // Create user's playlist info request
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/" + userId
                        + "/playlists")
                .addHeader("Authorization","Bearer "
                        + this.accessToken)
                .build();

        Call call = this.requestClient.newCall(request);
        ExecutorService pool = Executors.newFixedThreadPool(1);

        // Return a Future
        return pool.submit(() -> {
            Set<Playlist> playlists = new HashSet<>();

            final JSONObject jsonObject = new JSONObject(call.execute()
                    .body().string());

            // If user does not exist
            if (jsonObject.has("error"))
                throw new APIRequestException(context.getString(R.string.invalid_user_id));

            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // For each user's playlists
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject playlistObject = itemsArray.getJSONObject(i);

                // Get info from JSON
                String playlist_id = playlistObject.getString("id");
                String playlist_name = playlistObject.getString("name");

                // Create Playlist
                Playlist playlist = new Playlist(playlist_id, playlist_name);
                // Add to playlist set
                playlists.add(playlist);

                // Fill playlist with tracks
                playlist.addTracks(this.getTracksFromPlaylist(playlist_id,
                        userId).get());
            }

            return playlists;
        });
    }

    /**
     * Get all the tracks of a playlist.
     * @param playlistId The id of the playlist.
     * @return A Future with the set of Tracks.
     */
    private Future<Set<Track>> getTracksFromPlaylist(String playlistId,
                                                     String userId) {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        // Return a Future
        return pool.submit(() -> {

            // Determine if there are more tracks to get
            boolean next;
            // First url to request
            String url = "https://api.spotify.com/v1/playlists/"
                    + playlistId + "/tracks?" +
                    "fields=items(track(id,name,artists,preview_url," +
                    "album(name,images))),next"; // Only important fields

            // Create total track set to return
            Set<Track> totalTrack = new HashSet<>();

            do {
                // Create playlist info request
                final Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization","Bearer "
                                + this.accessToken)
                        .build();

                Call call = this.requestClient.newCall(request);

                List<Track> tracks = new ArrayList<>();
                StringBuilder trackIdList = new StringBuilder(); // List of track ids

                final JSONObject jsonObject = new JSONObject(call.execute()
                        .body().string());

                // If playlist does not exist
                if (jsonObject.has("error"))
                    throw new APIRequestException(context.getString(R.string.invalid_playlist_id));

                JSONArray itemsArray = jsonObject.getJSONArray("items");

                // For each track in playlist
                for (int i = 0; i < itemsArray.length(); i++) {
                    // If the track is null, skip it
                    if (itemsArray.getJSONObject(i).isNull("track"))
                        continue;

                    JSONObject trackObject = itemsArray.getJSONObject(i)
                            .getJSONObject("track");

                    // If the track does not have id, skip it
                    if (trackObject.isNull("id"))
                        continue;

                    // If the track is not "playable"
                    if (trackObject.isNull("preview_url"))
                        continue;

                    // Get info from JSON
                    String track_id = trackObject.getString("id");
                    String track_title = trackObject.getString("name");
                    String track_album = trackObject.getJSONObject("album")
                            .getString("name");

                    // Get all artists for the track
                    Set<String> artists = new HashSet<>();
                    JSONArray artistArray = trackObject
                            .getJSONArray("artists");
                    for (int j = 0; j < artistArray.length(); j++)
                        artists.add(artistArray.getJSONObject(j)
                                .getString("name"));

                    String cover_url = trackObject.getJSONObject("album")
                            .getJSONArray("images").getJSONObject(2)
                            .getString("url");

                    // Create Track
                    Track track = Factory.getTrack(track_id, track_title,
                            track_album, artists, cover_url, userId);

                    // Append ids for the track features request
                    trackIdList.append(track_id).append(",");

                    tracks.add(track);
                }

                // Only if there are 1+ tracks
                if (tracks.size() > 0) {
                    // Request track features
                    List<TrackFeatures> trackFeaturesList =
                            getTracksFeatures(trackIdList.toString()).get();

                    int i = 0;
                    // Set feature for each track
                    for (Track t : tracks)
                        t.setFeatures(trackFeaturesList.get(i++));
                }

                // Add current tracks to total Set
                totalTrack.addAll(tracks);

                // Check if tracks left
                next = !jsonObject.isNull("next");
                if (next)
                    url = jsonObject.getString("next");

            } while (next);

            return totalTrack;
        });
    }

    /**
     * Get the TrackFeatures of a list of Tracks.<br />
     * The maximum of Tracks is 100.
     * @param trackIds The ids of the Tracks separated with a comma.
     * @return A Future with a list TrackFeatures.
     */
    private Future<List<TrackFeatures>> getTracksFeatures(String trackIds) {
        // Create track features request for a list of tracks
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/audio-features?ids="
                        + trackIds) // Add GET parameters
                .addHeader("Authorization","Bearer "
                        + this.accessToken)
                .build();

        Call call = this.requestClient.newCall(request);
        ExecutorService pool = Executors.newFixedThreadPool(1);

        // Return a Future
        return pool.submit(() -> {
            List<TrackFeatures> tracksFeatures = new ArrayList<>();

            final JSONObject jsonObject = new JSONObject(call.execute()
                    .body().string());
            JSONArray trackArray = jsonObject
                    .getJSONArray("audio_features");

            // For each track
            for (int i = 0; i < trackArray.length(); i++) {
                // Some tracks does not have features
                if (trackArray.isNull(i)) {
                    tracksFeatures.add(null);
                    continue;
                }

                JSONObject trackObject = trackArray.getJSONObject(i);

                // Get info from JSON
                double danceability = trackObject.getDouble("danceability");
                double energy = trackObject.getDouble("energy");
                double speechiness = trackObject.getDouble("speechiness");
                double acousticness = trackObject.getDouble("acousticness");
                double instrumentalness = trackObject.getDouble("instrumentalness");
                double liveness = trackObject.getDouble("liveness");
                double valence = trackObject.getDouble("valence");

                // Create TrackFeatures
                TrackFeatures features = new TrackFeatures(
                        danceability,
                        energy,
                        speechiness,
                        acousticness,
                        instrumentalness,
                        liveness,
                        valence
                );

                tracksFeatures.add(features);
            }

            return tracksFeatures;
        });
    }

    @Override
    public void resumeTrack() {
        mSpotifyAppRemote
                .getPlayerApi()
                .getPlayerState()
                .setResultCallback(
                        playerState -> {
                            if (playerState.isPaused)
                                mSpotifyAppRemote
                                        .getPlayerApi()
                                        .resume().setResultCallback(
                                        empty ->  Log.d("Event API" ,
                                                " Track resumed")
                                );
                        });
    }

    @Override
    public void pauseTrack() {
        mSpotifyAppRemote
                .getPlayerApi()
                .getPlayerState()
                .setResultCallback(
                        playerState -> {
                            if (!playerState.isPaused)
                                mSpotifyAppRemote
                                        .getPlayerApi()
                                        .pause().setResultCallback(
                                        empty ->  Log.d("Event API" ,
                                                " Track paused")
                                );
                            });
    }

    @Override
    public void playTrack(String id) {
        mSpotifyAppRemote
                .getPlayerApi()
                .play("spotify:track:"+id)
                .setResultCallback(
                        data -> Log.d("Event API" ," play track " + id)
                );
    }

    @Override
    public void skipNextTrack(){
        mSpotifyAppRemote
                .getPlayerApi()
                .skipNext()
                .setResultCallback(
                        data -> Log.d("Event API" ," skip next")
                );
    }

    @Override
    public void skipPreviousTrack (){
        mSpotifyAppRemote
                .getPlayerApi()
                .skipPrevious()
                .setResultCallback(
                        data -> Log.d("Event API" ," skip previous")
                );
    }

    @Override
    public void queueTrack(String id) {
        mSpotifyAppRemote
                .getPlayerApi()
                .queue("spotify:track:"+id)
                .setResultCallback(
                        data -> Log.d("Event API" ," queue track " + id)
                );
    }

    private void subscribeToPlayerChange() {
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final com.spotify.protocol.types.Track track =
                            playerState.track;
                    if (track != null) {
                        Log.d("SpotifyAPI", track.name +
                                " by " + track.artist.name);
                        Services.syncCurrentTrack(track.uri
                                .replaceFirst("spotify:track:",""));
                        PresentationServices.updateCover();
                        PresentationServices.updateLikeButton();
                    }
                });
    }

    @Override
    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    @Override
    public void emptyQueue() {
        for(int i = 0; i <15 ; i++)
            this.skipNextTrack();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
