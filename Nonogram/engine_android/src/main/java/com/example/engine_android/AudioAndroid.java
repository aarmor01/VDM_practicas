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
    private HashMap<String, SoundAndroid> sounds;
    private SoundPool soundPool;
    private AssetManager assetManager;
    private MediaPlayer mediaPlayer;

    public void init (AssetManager assetManager_) {
        assetManager = assetManager_;
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        mediaPlayer = new MediaPlayer();
        sounds = new HashMap<>();
    }

    @Override
    public void setMusic(String filePath) throws IOException {
        mediaPlayer.reset();
        AssetFileDescriptor afd = assetManager.openFd(filePath);
        mediaPlayer.setDataSource(afd.getFileDescriptor(),
                afd.getStartOffset(), afd.getLength());
        mediaPlayer.prepare();
        mediaPlayer.setLooping(true);
    }

    @Override
    public void startMusic() {
        mediaPlayer.start();
    }

    @Override
    public void stopMusic() {
        mediaPlayer.stop();
    }

    @Override
    public void pauseMusic() {
        mediaPlayer.pause();
    }

    @Override
    public void setMusicVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void setSoundVolume(float volume){
        for (String clave:sounds.keySet())
            sounds.get(clave).setVolume(volume);
    }

    //COMO SETEAR EL VOLUMEN EN LA CARGA, COMO PARAMETRO DE LOAD SOUND??
    @Override
    public String loadSound(String filePath, float volume) {
        File soundFile = new File(filePath);
        sounds.put(soundFile.getName(), new SoundAndroid(filePath, assetManager, soundPool,volume));
        return soundFile.getName();
    }

    public void playSound(String soundName) {
        SoundAndroid s = sounds.get(soundName);
        soundPool.play(s.getSoundId(), s.getVolume(),  s.getVolume(), s.getPriority(), s.getLoop(), s.getRate());
    }
}
