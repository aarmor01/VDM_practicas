package com.example.engine_desktop;

import com.example.engine_common.interfaces.ISound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundDesktop implements ISound {
    AudioInputStream audioStream;
    Clip clip;
    float db;

    SoundDesktop(File audioFile, float volume_db) {
        try {
            audioStream =  AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            System.err.println("Couldn't load audio file");
            e.printStackTrace();
        }

        db = volume_db;
    }

    public Clip getSound() { return clip; }

    @Override
    public float getVolume() {
        return db;
    }

    @Override
    public void setVolume(float volume_db) {
        db = volume_db;
    }
}
