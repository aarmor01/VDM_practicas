package com.example.engine_desktop;

import com.example.engine_common.interfaces.IAudio;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioDesktop implements IAudio {
    private HashMap<String, SoundDesktop> sounds;
    private Clip bgMusic;

    public AudioDesktop() {
        sounds = new HashMap<>();
    }

    @Override
    public void loadMusic(String filePath, float volume) {
        try {
            bgMusic = AudioSystem.getClip();
            bgMusic.open(AudioSystem.getAudioInputStream(new File(filePath)));

            FloatControl gainControl = (FloatControl) bgMusic.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue((float)(10 * Math.log(volume)));
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String loadSound(String filePath, float volume) {
        File soundFile = new File(filePath);
        sounds.put(soundFile.getName(), new SoundDesktop(soundFile, (float)(10 * Math.log(volume))));
        return soundFile.getName();
    }

    @Override
    public void playMusic() {
        bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
        bgMusic.start();
    }

    @Override
    public void playSound(String soundName) {
        Clip clip = sounds.get(soundName).getSound();
        clip.loop(0);

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(sounds.get(soundName).getVolume());

        if(!clip.isRunning())
            clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void setMusicVolume(float volume) {
        FloatControl gainControl = (FloatControl) bgMusic.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
    }

    @Override
    public void setSoundVolume(String soundName, float volume) {
        sounds.get(soundName).setVolume((float)(10 * Math.log(volume)));
    }
}
