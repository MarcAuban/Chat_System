package com.codebind.Controleurs;

import javax.swing.*;
import java.awt.*;

public class AProposBackground implements Runnable{
    private JPanel panel;
    private JTextPane pane;
    private double angle=0;
    private boolean on=true;

    public AProposBackground(JPanel panel, JTextPane pane){
        this.panel = panel;
        this.pane = pane;
    }

    public AProposBackground(JPanel panel){
        this.panel = panel;
        this.pane = null;
    }

    public void run(){
        while(on) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double pi = Math.PI;
            if(angle>2* pi)
                angle=0;
            angle+= pi /90;
            int r = (int) ((Math.sin(angle)+1)/2 * 255);
            int g = (int) ((Math.sin(angle + 2 * pi / 3)+1)/2 * 255);
            int b = (int) ((Math.sin(angle - 2 * pi / 3)+1)/2 * 255);

            int tr = 0, tg = 0, tb = 0;
            if (r > 0)
                tr = r;
            if (g > 0)
                tg = g;
            if (b > 0)
                tb = b;

            panel.setBackground(new Color(tr, tg, tb));
            if(pane != null) {
                pane.setBackground(new Color(tr, tg, tb));
                pane.setForeground(new Color(255 - tr, 255 - tg, 255 - tb));
            }
        }
    }
    public void stop(){
        this.on=false;
    }
}
