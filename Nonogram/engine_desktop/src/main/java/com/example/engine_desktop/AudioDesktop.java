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
        sounds = new HashMap<>();
    }

    @Override
    public void loadMusic(String filePath, float volume) {
        bgMusic = new SoundDesktop(new File(filePath), toDB(volume));

        FloatControl gainControl = (FloatControl) bgMusic.getSound().getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(bgMusic.getVolume());
    }

    @Override
    public String loadSound(String filePath, float volume) {
        File soundFile = new File(filePath);
        sounds.put(soundFile.getName(), new SoundDesktop(soundFile, toDB(volume)));
        return soundFile.getName();
    }

    @Override
    public void playMusic() {
        bgMusic.getSound().loop(Clip.LOOP_CONTINUOUSLY);
        bgMusic.getSound().start();
    }

    @Override
    public void playSound(String soundName) {
        Clip clip = sounds.get(soundName).getSound();
        clip.loop(0);

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(sounds.get(soundName).getVolume());

        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void setMusicVolume(float volume) {
        FloatControl gainControl = (FloatControl) bgMusic.getSound().getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(toDB(volume));
    }

    @Override
    public void setSoundVolume(String soundName, float volume) {
        sounds.get(soundName).setVolume(toDB(volume));
    }

    private float toDB(float volume) {
        return (float)(10 * Math.log(volume));
    }
}
