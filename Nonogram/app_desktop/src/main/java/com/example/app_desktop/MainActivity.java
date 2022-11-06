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

        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                renderView.createBufferStrategy(2);
                break;
            }
            catch(Exception e) {
            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }
        else {
            // En "modo debug" podríamos querer escribir esto.
            //System.out.println("BufferStrategy tras " + (100 - intentos) + " intentos.");
        }

        EngineDesktop eng = new EngineDesktop(renderView);
        MyScene scene = new MyScene();

        scene.init(eng);
        eng.getSceneManager().pushScene(scene);
        eng.resume();
    }
}