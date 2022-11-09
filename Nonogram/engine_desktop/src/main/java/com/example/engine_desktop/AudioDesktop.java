package com.example.engine_desktop;

import com.example.engine_common.interfaces.IAudio;

import java.io.File;

import java.util.HashMap;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioDesktop implements IAudio {
    private HashMap<String, SoundDesktop> sounds;
    private SoundDesktop bgMusic;

    public AudioDesktop() {
        this.sounds = new HashMap<>();
    }

    // always returns key to obtain the object. If present, it won't load again
    @Override
    public void loadMusic(String filePath, float volume) {
        this.bgMusic = new SoundDesktop(new File(filePath), toDB(volume));

        FloatControl gainControl = (FloatControl) this.bgMusic.getSound().getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(this.bgMusic.getVolume());
    }

    // always returns key to obtain the object. If present, it won't load again
    @Override
    public String loadSound(String filePath, float volume) {
        File soundFile = new File(filePath);
        if(!this.sounds.containsKey(soundFile.getName()))
            this.sounds.put(soundFile.getName(), new SoundDesktop(soundFile, toDB(volume)));
        return soundFile.getName();
    }

    @Override
    public void playMusic() {
        this.bgMusic.getSound().loop(Clip.LOOP_CONTINUOUSLY);
        this.bgMusic.getSound().start();
    }

    @Override
    public void playSound(String soundName) {
        Clip clip = this.sounds.get(soundName).getSound();
        clip.loop(0);

        // set volume to current one
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(this.sounds.get(soundName).getVolume());

        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void setMusicVolume(float volume) {
        // set volume to current one
        FloatControl gainControl = (FloatControl)this.bgMusic.getSound().getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(toDB(volume));
    }

    @Override
    public void setSoundVolume(String soundName, float volume) {
        this.sounds.get(soundName).setVolume(toDB(volume));
    }

    private float toDB(float volume) {
        return (float)(10 * Math.log(volume));
    }
}
