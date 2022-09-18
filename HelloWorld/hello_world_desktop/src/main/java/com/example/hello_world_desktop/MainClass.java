package com.example.hello_world_desktop;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Panel;
import java.awt.Button;
import java.awt.TextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainClass {
    public static void main(String[] args) {
        // window frame
        JFrame frame = new JFrame("App");
        frame.setSize(400,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // placed on display's center position
        frame.setVisible(true);

        // create panel to contain GUI elements
        Panel pan = new Panel();
        frame.add(pan);

        // create textbox
        final TextField textbox = new TextField(50);

        // create button
        Button but = new Button("Send");
        pan.add(but);
        but.addMouseListener(new MouseListener() { // add button action
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JOptionPane.showMessageDialog(null, textbox.getText(), "Message",  JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
//        but.addActionListener(new ActionListener() { // another way to add click action
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                JOptionPane.showMessageDialog(null, textbox.getText(), "Message",  JOptionPane.INFORMATION_MESSAGE);
//            }
//        });

        // add textbox to panel
        pan.add(textbox);

        // adjust window to elements
        frame.pack();
    }
}