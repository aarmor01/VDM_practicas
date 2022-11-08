package com.example.app_desktop;

import javax.swing.JFrame;

import com.example.engine_desktop.EngineDesktop;
import com.example.nonogram.MyScene;

import java.awt.Color;

public class MainActivity {

    public static void main(String[] args) {
        JFrame renderView = new JFrame("Nonogram");

        renderView.setSize(400, 600);
        renderView.setBackground(Color.GRAY);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(false);
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

        EngineDesktop eng = new EngineDesktop(renderView);
        MyScene scene = new MyScene();

        scene.init(eng);
        eng.getSceneManager().pushScene(scene);
        eng.resume();
    }
}