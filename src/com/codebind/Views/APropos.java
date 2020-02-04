package com.codebind.Views;

import com.codebind.Controleurs.AProposBackground;

import javax.swing.*;

public class APropos extends JFrame{
    private JPanel panel;
    private JTextPane textPane;
    private AProposBackground b;

    APropos(){
        setContentPane(panel);
        textPane.setEditable(false);
        textPane.setOpaque(true);
        textPane.setText("Created by Charles & Marc");

        b = new AProposBackground(panel,textPane);
        Thread AProposBackgroundThread = new Thread(b);
        AProposBackgroundThread.start();

        addWindowListener(new java.awt.event.WindowAdapter() { //Bouton X de la frame
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Stop();
                dispose();
            }
        });
    }
    public void Stop(){
        b.stop();
    }
}

