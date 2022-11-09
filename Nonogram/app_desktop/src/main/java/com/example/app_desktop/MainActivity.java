package com.example.app_desktop;

import javax.swing.JFrame;

import com.example.engine_desktop.EngineDesktop;
import com.example.nonogram.MainMenu;

public class MainActivity {
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

        // create engine and scene
        EngineDesktop engine = new EngineDesktop(renderView, 0xFFFFFFFF);
        MainMenu mainMenu = new MainMenu();

        // start up
        mainMenu.init(engine);
        engine.getSceneManager().pushScene(mainMenu);
        engine.resume();
    }
}