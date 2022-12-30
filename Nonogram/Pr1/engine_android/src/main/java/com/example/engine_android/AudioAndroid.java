package com.example.engine_android;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.engine_common.interfaces.IAudio;
import com.example.engine_common.interfaces.ISound;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AudioAndroid implements IAudio {
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

    @Override
    public void loadMusic(String filePath, float volume) {
        // reset the audio player for background music
        this.mediaPlayer.reset();
        AssetFileDescriptor assetFD = null;
        try {
            // load the music resource from assets folder
            assetFD = this.assetManager.openFd(filePath);
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

    @Override
    public String loadSound(String filePath, float volume) {
        //obtains the path of the sound in the assets folder
        File soundFile = new File(filePath);
        //if the sound has not been already been loaded, it is stored in the sounds manager
        if(!this.sounds.containsKey(soundFile.getName()))
            this.sounds.put(soundFile.getName(), new SoundAndroid(filePath, this.assetManager, this.soundPool, volume));
        return soundFile.getName(); //it returns the name of the sound in order to looking for it when you need to play it
    }

    @Override
    public void playMusic() {
        this.mediaPlayer.start();
    }

    @Override
    public void playSound(String soundName) {
        //looks for the sound in the sound manager, and plays it on the audio player of sound effects
        SoundAndroid s = this.sounds.get(soundName);
        this.soundPool.play(s.getSoundId(), s.getVolume(),  s.getVolume(), s.getPriority(), s.getLoop(), s.getRate());
    }

    @Override
    public void pauseMusic() {
        this.mediaPlayer.pause();
    }

    @Override
    public void pauseSound(String soundName) {
        SoundAndroid sound = this.sounds.get(soundName);
        this.soundPool.pause(sound.getSoundId());
    }

    @Override
    public void setMusicVolume(float volume) {
        this.mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void setSoundVolume(String name, float volume){
        this.sounds.get(name).setVolume(volume);
    }
}
