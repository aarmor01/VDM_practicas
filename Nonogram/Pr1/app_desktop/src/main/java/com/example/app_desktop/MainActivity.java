package com.example.app_desktop;

import javax.swing.JFrame;

import com.example.engine_desktop.EngineDesktop;

import com.example.nonogram.BootScene;
import com.example.nonogram.GameManager;

public class MainActivity {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {
        // create window
        JFrame renderView = new JFrame("Nonogram");

        // make adjustments to it
        renderView.setSize(400, 600);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);
        renderView.setVisible(true);

        // load buffer strategy with 2 buffers
        while(true) {
            try {
                renderView.createBufferStrategy(2);
                break;
            } catch (Exception e) {
                System.err.println("Couldn't load buffer strategy yet");
                e.printStackTrace();
            }
        }

        // create engine
        EngineDesktop engine = new EngineDesktop(renderView, 0xFFFFFFFF);

        // GameManager
        GameManager.init(WIDTH, HEIGHT);

        // start up
        engine.getSceneManager().pushScene(new BootScene(engine));
        engine.getAudio().playMusic();
        engine.resume();
    }
}