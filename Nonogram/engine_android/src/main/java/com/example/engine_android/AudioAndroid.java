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
    //asset Manager to obtain the resources
    private AssetManager assetManager;
    //audio players
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    //audio resources
    private HashMap<String, SoundAndroid> sounds;

    public  AudioAndroid(AssetManager assetManager_) {
        //obtain the Asset Manager
        assetManager = assetManager_;
        //create the audio players
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        mediaPlayer = new MediaPlayer();
        //start resource managers
        sounds = new HashMap<>();
    }

    @Override
    public void loadMusic(String filePath, float volume) {
        //reset the audio player for background music
        mediaPlayer.reset();
        AssetFileDescriptor afd = null;
        try {
            //load the music resource from assets folder
            String convFilepath = filePath.replaceAll("./assets/", "");
            afd = assetManager.openFd(convFilepath);
            mediaPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            //sets volume
            mediaPlayer.setVolume(volume, volume);
            //prepares the audio player
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //actives the loop option, as it has to loop indefinitely
        mediaPlayer.setLooping(true);
    }

    @Override
    public void playMusic() {
        mediaPlayer.start();
    }

    @Override
    public void setMusicVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void setSoundVolume(String name, float volume){
        sounds.get(name).setVolume(volume);
    }

    @Override
    public String loadSound(String filePath, float volume) {
        //obtains the path of the sound in the assets folder
        File soundFile = new File(filePath);
        String convFilepath = filePath.replaceAll("./assets/", "");
        //if the sound has not been already been loaded, it is stored in the sounds manager
        if(!sounds.containsKey(soundFile.getName()))
            sounds.put(soundFile.getName(), new SoundAndroid(convFilepath, assetManager, soundPool,volume));
        //it returns the name of the sound in order to looking for it when you need to play it
        return soundFile.getName();
    }

    public void playSound(String soundName) {
        //looks for the sound in the sound manager, and plays it on the audio player of sound effects
        SoundAndroid s = sounds.get(soundName);
        soundPool.play(s.getSoundId(), s.getVolume(),  s.getVolume(), s.getPriority(), s.getLoop(), s.getRate());
    }
}
