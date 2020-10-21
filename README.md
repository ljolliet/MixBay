# MixBay

When a group of people listen to music, during a car ride or a party for instance, most of the time the music is chosen by the one 
carrying the device that plays it. The group frequently asks to play this or that song. This project aims to allow the group to connect 
themselves on a same device, linking their Spotify, Deezer or Apple Music account. The app will propose a playlist capable of satisfying 
most of the users connected. This App called Mixbay (only available on Android) embed different playlist generation algorithm. For now,
only Spotify accounts can be used to sign in the app.

## Requirements
- Gradle
- Android Studio
- Android SDK

## Set up 
- Clone/download this repo.
- Register App Fingerprint ([Spotify API](https://developer.spotify.com/documentation/android/quick-start/)) :
    > Run the following command in your Terminal (**no password**):
 
     ```# On Bash style shells
        $ keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore -list -v | grep SHA1  
        # If not working : 
        $ keytool -alias androiddebugkey -keystore ~/.android/debug.keystore -list -v |grep SHA1
         
        # On Windows Powershell
        $ keytool -exportcert -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore -list -v | grep SHA1
    ```
    >You should expect to receive a fingerprint that looks like this:
     `SHA1: E7:47:B5:45:71:A9:B4:47:EA:AD:21:D7:7C:A2:8D:B4:89:1C:BF:75`     
   
   Then send the App fingerprint to Paul Vigneau at *paul.vigneau@etu.u-bordeaux.fr* to add your fingerprint to the Spotify App                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
## Run
- Run with Android Studio
- Use an Emulator or and Android phone but it need **Spotify App installed**
