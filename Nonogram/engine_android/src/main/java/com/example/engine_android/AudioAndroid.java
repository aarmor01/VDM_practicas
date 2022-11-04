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
    }

    @Override
    public void setBackgroundMusic(String filePath) throws IOException {
        mediaPlayer.reset();
        AssetFileDescriptor afd = assetManager.openFd(filePath);
        mediaPlayer.setDataSource(afd.getFileDescriptor(),
                afd.getStartOffset(), afd.getLength());
        mediaPlayer.prepare();
        mediaPlayer.setLooping(true);
    }

    @Override
    public void startBGMusic() {
        mediaPlayer.start();
    }

    @Override
    public void stopBGMusic() {
        mediaPlayer.stop();
    }

    @Override
    public void pauseBGMusic() {
        mediaPlayer.pause();
    }

    @Override
    public void setBGVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void setVFX(float volume){
        for (String clave:sounds.keySet()) {
            sounds.get(clave).setVolume(volume);
        }
    }

    //COMO SETEAR EL VOLUMEN EN LA CARGA, COMO PARAMETRO DE LOAD SOUND??
    @Override
    public String loadSound(String filePath) throws IOException {
        File soundFile = new File(filePath);
        AssetFileDescriptor assetDescriptor = assetManager.openFd(filePath);
        int soundId = soundPool.load(assetDescriptor, 0);
        sounds.put(soundFile.getName(), new SoundAndroid(soundId, 0.5f, 0, 1, 1));
        return soundFile.getName();
    }

    public void playSound(String soundName) {
        SoundAndroid s = sounds.get(soundName);
        soundPool.play(s.getSoundId(), s.getVolume(),  s.getVolume(), s.getPriority(), s.getLoop(), s.getRate());
    }
}
