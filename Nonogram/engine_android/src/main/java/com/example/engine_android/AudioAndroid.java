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
    private AssetManager assetManager;

    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;

    private HashMap<String, SoundAndroid> sounds;

    public  AudioAndroid(AssetManager assetManager_) {
        assetManager = assetManager_;

        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        mediaPlayer = new MediaPlayer();

        sounds = new HashMap<>();
    }

    @Override
    public void loadMusic(String filePath, float volume) {
        mediaPlayer.reset();
        AssetFileDescriptor afd = null;
        try {
            String convFilepath = filePath.replaceAll("./assets/", "");
            afd = assetManager.openFd(convFilepath);
            mediaPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    //COMO SETEAR EL VOLUMEN EN LA CARGA, COMO PARAMETRO DE LOAD SOUND??
    @Override
    public String loadSound(String filePath, float volume) {
        File soundFile = new File(filePath);
        String convFilepath = filePath.replaceAll("./assets/", "");
        sounds.put(soundFile.getName(), new SoundAndroid(convFilepath, assetManager, soundPool,volume));
        return soundFile.getName();
    }

    public void playSound(String soundName) {
        SoundAndroid s = sounds.get(soundName);
        soundPool.play(s.getSoundId(), s.getVolume(),  s.getVolume(), s.getPriority(), s.getLoop(), s.getRate());
    }
}
