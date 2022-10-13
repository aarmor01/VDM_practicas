package com.example.engine_interfaces;

import java.awt.event.MouseEvent;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public interface IInput {
    public void inputClick();
    public void inputPressed();
    public void inputReleased();
    public void inputEntered();
    public void inputExited();
}
