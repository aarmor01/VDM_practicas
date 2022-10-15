package com.example.app_desktop;

import javax.swing.JFrame;

import com.example.engine_desktop.Engine;
import com.example.nonogram.MyScene;

public class MainActivity {

    public static void main(String[] args) {
        JFrame renderView = new JFrame("Mi aplicación");


        renderView.setSize(600, 400);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);
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

        Engine eng = new Engine();
        MyScene scene = new MyScene();
        scene.init(eng);
        eng.init(renderView, scene);
        eng.resume();
    }
}