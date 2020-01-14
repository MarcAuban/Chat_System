package com.codebind.FrontEnd;

import javax.swing.*;
import java.awt.*;

public class AProposBackground implements Runnable{
    private JPanel panel;
    private JTextPane pane;
    private int r=0, g=220, b=-220;
    private int rm=4, gm=4, bm=-4;
    private boolean on=true;

    AProposBackground(JPanel panel, JTextPane pane){
        this.panel = panel;
        this.pane = pane;
    }

    public void run(){
        while(on) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (r >= 250 || r <=-250)
            rm = -rm;
            if (g >= 250 || g<=-250)
            gm = -gm;
            if (b >= 250 || b<=-250)
            bm = -bm;

            r = r + rm;
            g = g + gm;
            b = b + bm;

            int tr = 0, tg = 0, tb = 0;
            if (r > 0)
                tr = r;
            if (g > 0)
                tg = g;
            if (b > 0)
                tb = b;

            panel.setBackground(new Color(tr, tg, tb));
            pane.setBackground(new Color(tr,tg,tb));
            pane.setForeground(new Color(255-tr,255-tg,255-tb));
        }
    }
    public void stop(){
        this.on=false;
    }
}
