package com.example.engine_android.Modules;

import com.example.engine_android.DataStructures.SoundAndroid;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.File;
import java.util.HashMap;

public class AudioAndroid {
    // manager to obtain the resources
    private AssetManager assetManager;

    // audio players
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;

    // audio resources
    private HashMap<String, SoundAndroid> sounds;

    public AudioAndroid(AssetManager assetManager) {
        //obtain the AssetManager
        this.assetManager = assetManager;

        //create the audio players
        this.soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        this.mediaPlayer = new MediaPlayer();

        //start resource managers
        this.sounds = new HashMap<>();
    }

    public void loadMusic(String filePath, float volume) {
        // reset the audio player for background music
        this.mediaPlayer.reset();
        AssetFileDescriptor assetFD = null;
        try {
            // load the music resource from assets folder
            String convFilepath = filePath.replaceAll("./assets/", "");
            assetFD = this.assetManager.openFd(convFilepath);
            this.mediaPlayer.setDataSource(assetFD.getFileDescriptor(),
                    assetFD.getStartOffset(), assetFD.getLength());

            this.mediaPlayer.setVolume(volume, volume); // sets volume
            this.mediaPlayer.prepare();                 // prepares the audio player
        } catch (Exception e) {
            System.err.println("Couldn't load music file");
            e.printStackTrace();
        }

        // actives the loop option, as it has to loop indefinitely
        this.mediaPlayer.setLooping(true);
    }

    public String loadSound(String filePath, float volume) {
        //obtains the path of the sound in the assets folder
        File soundFile = new File(filePath);
        String convFilepath = filePath.replaceAll("./assets/", "");
        //if the sound has not been already been loaded, it is stored in the sounds manager
        if(!this.sounds.containsKey(soundFile.getName()))
            this.sounds.put(soundFile.getName(), new SoundAndroid(convFilepath, this.assetManager, this.soundPool,volume));
        return soundFile.getName(); //it returns the name of the sound in order to looking for it when you need to play it
    }

    public void playMusic() {
        this.mediaPlayer.start();
    }

    public void playSound(String soundName) {
        //looks for the sound in the sound manager, and plays it on the audio player of sound effects
        SoundAndroid s = this.sounds.get(soundName);
        this.soundPool.play(s.getSoundId(), s.getVolume(),  s.getVolume(), s.getPriority(), s.getLoop(), s.getRate());
    }

    public void setMusicVolume(float volume) {
        this.mediaPlayer.setVolume(volume, volume);
    }

    public void setSoundVolume(String name, float volume){
        this.sounds.get(name).setVolume(volume);
    }
}
