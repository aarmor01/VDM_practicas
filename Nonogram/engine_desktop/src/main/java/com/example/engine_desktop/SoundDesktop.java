package com.example.engine_desktop;

import com.example.engine_common.interfaces.ISound;

import java.io.File;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class SoundDesktop implements ISound {
    AudioInputStream audioStream;
    Clip clip;
    float volume_db;

    SoundDesktop(File audioFile, float volume_db) {
        try {
            audioStream =  AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            System.err.println("Couldn't load audio file");
            e.printStackTrace();
        }

        this.volume_db = volume_db;
    }

    public Clip getSound() { return clip; }

    @Override
    public float getVolume() {
        return volume_db;
    }

    @Override
    public void setVolume(float volume_db) {
        this.volume_db = volume_db;
    }
}
