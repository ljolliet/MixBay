package fr.pdp.mixbay.models;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SpotifyAPI implements APIManagerI {

    private static final int SPOTIFY_REQUEST_CODE = 1337;
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

        return true;
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
    public User getUser(String id) {
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/" + id)
                .addHeader("Authorization","Bearer " + this.accessToken)
                .build();

        Call call = this.requestClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("SpotifyAPI", "Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.d("SpotifyAPI", "Data: " + jsonObject.toString());

                    User user = new User(id, jsonObject.getString("display_name"));
                } catch (JSONException e) {
                    Log.d("SpotifyAPI", "Failed to fetch data: " + e);
                }
            }
        });

        return null;
    }

    @Override
    public Set<Playlist> getUserPlaylist(String id) {
        return null;
    }

    @Override
    public void playCurrentTrack() {

    }

    @Override
    public void playTrack(String id) {

    }

    private void getPlayerInfo() {
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }
}
