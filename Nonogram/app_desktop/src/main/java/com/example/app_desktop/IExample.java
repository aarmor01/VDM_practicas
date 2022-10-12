package com.example.app_desktop;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class IExample extends JPanel {
    public IExample() {
        MouseListener ms = new MyMouseListener();
        addMouseListener(ms);
        setSize(600, 400);
        setBackground(Color.BLACK);
        setVisible(true);
        setFocusable(true);
    }
}
