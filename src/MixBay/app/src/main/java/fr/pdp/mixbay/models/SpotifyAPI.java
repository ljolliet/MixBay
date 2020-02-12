package fr.pdp.mixbay.models;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class SpotifyAPI implements APIManagerI {

    private static final String CLIENT_ID = "cb64858e27964a13b85d3e7e5e19b1d3";
    private static final String REDIRECT_URI = "mixbay://callback";
    private static final String[] SCOPES = new String[]{"app-remote-control", "user-read-email", "user-read-private"};

    private SpotifyAppRemote mSpotifyAppRemote;
    private Context context;

    private final OkHttpClient requestClient = new OkHttpClient();
    private String accessToken;


    @Override
    public boolean connect(Context context) {
        this.context = context;

        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(SCOPES);
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity((Activity) context, SPOTIFY_REQUEST_CODE, request);

        return true;    //TODO return only if connected
    }

    public boolean playbackConnect(){
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(context, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected");
                        getPlayerInfo();
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);
                    }
                });
        return mSpotifyAppRemote!= null && mSpotifyAppRemote.isConnected();
    }


    @Override
    public boolean disconnect() {
        //SpotifyAppRemote.disconnect(mSpotifyAppRemote);

        if (context == null)
            return false;

        AuthorizationClient.clearCookies(context);
        return true;
    }

    @Override
    public Future<User> getUser(String id) {
        // Create request
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/" + id)
                .addHeader("Authorization","Bearer " + this.accessToken)
                .build();

        Call call = this.requestClient.newCall(request);
        ExecutorService pool = Executors.newFixedThreadPool(1);

        // Return a Future
        return pool.submit(() -> {
            final JSONObject jsonObject = new JSONObject(call.execute().body().string());
            return new User(id, jsonObject.getString("display_name"));
        });
    }

    @Override
    public Future<Set<Playlist>> getUserPlaylists(String userId) {
        // Create request
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/" + userId + "/playlists")
                .addHeader("Authorization","Bearer " + this.accessToken)
                .build();

        Call call = this.requestClient.newCall(request);
        ExecutorService pool = Executors.newFixedThreadPool(1);

        // Return a Future
        return pool.submit(() -> {
            Set<Playlist> playlists = new HashSet<>();

            final JSONObject jsonObject = new JSONObject(call.execute().body().string());
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject playlistObject = itemsArray.getJSONObject(i);

                // Get info from JSON
                String playlist_id = playlistObject.getString("id");
                String playlist_name = playlistObject.getString("name");

                Playlist playlist = new Playlist(playlist_id, playlist_name);
                playlists.add(playlist);

                playlist.addTracks(this.getTracksFromPlaylist(playlist_id).get());
            }
            return playlists;
        });
    }

    private Future<Set<Track>> getTracksFromPlaylist(String playlistId) {
        // Create request
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/playlists/" + playlistId + "/tracks")
                .addHeader("Authorization","Bearer " + this.accessToken)
                .build();

        Call call = this.requestClient.newCall(request);
        ExecutorService pool = Executors.newFixedThreadPool(1);

        // Return a Future
        return pool.submit(() -> {
            Set<Track> tracks = new HashSet<>();

            final JSONObject jsonObject = new JSONObject(call.execute().body().string());
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject trackObject = itemsArray.getJSONObject(i).getJSONObject("track");

                // Get info from JSON
                String track_id = trackObject.getString("id");
                String track_title = trackObject.getString("name");
                String track_album = trackObject.getJSONObject("album").getString("name");

                Set<String> artists = new HashSet<>();
                JSONArray artistArray = trackObject.getJSONArray("artists");
                for (int j = 0; j < artistArray.length(); j++)
                    artists.add(artistArray.getJSONObject(j).getString("name"));

                String cover_url = trackObject.getJSONObject("album").getJSONArray("images").getJSONObject(2).getString("url");

                Track track = new Track(track_id, track_title, track_album, artists, cover_url, null);
                tracks.add(track);
            }
            return tracks;
        });
    }

    @Override
    public void playPauseTrack() {

        mSpotifyAppRemote
                .getPlayerApi()
                .getPlayerState()
                .setResultCallback(
                        playerState -> {
                            if (playerState.isPaused) {
                                mSpotifyAppRemote
                                        .getPlayerApi()
                                        .resume()
                                        .setResultCallback(
                                                empty -> Log.d("Event API" ," Track played")
                                        );

                            } else {
                                mSpotifyAppRemote
                                        .getPlayerApi()
                                        .pause().setResultCallback(
                                        empty ->  Log.d("Event API" ," Track paused")
                                );
                            }
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
    public void toggleRepeat(){
        mSpotifyAppRemote
                .getPlayerApi()
                .toggleRepeat()
                .setResultCallback(
                        data -> Log.d("Event API" ," toggle repeat")
                );
    }

    @Override
    public void toggleShuffle(){
        mSpotifyAppRemote
                .getPlayerApi()
                .toggleShuffle()
                .setResultCallback(
                        data -> Log.d("Event API" ," toggle shuffle")
                );
    }

    @Override
    public void seekFordward(){
        mSpotifyAppRemote
                .getPlayerApi()
                .seekToRelativePosition(15000)
                .setResultCallback(
                        data -> Log.d("Event API" ," seek forward")
                );
    }

    @Override
    public void seekBackward(){
        mSpotifyAppRemote
                .getPlayerApi()
                .seekToRelativePosition(-15000)
                .setResultCallback(data -> Log.d("Event API" ," seek backward")
                );
    }

    @Override
    public void queueTrack(String id) {
        mSpotifyAppRemote
                .getPlayerApi()
                .queue("spotify:track:"+id)
                .setResultCallback(
                        data -> Log.d("Event API" ," play track " + id)
                );
    }


    private void getPlayerInfo() {
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final com.spotify.protocol.types.Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }
}
