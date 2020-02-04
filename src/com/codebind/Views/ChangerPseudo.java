package com.codebind.Views;

import com.codebind.Models.ChatSystem;
import com.codebind.Controleurs.ControleurChangerPseudo;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChangerPseudo extends JFrame{
    private JButton OKButton;
    private JTextField textField;
    private JPanel panel1;
    private JButton cancelButton;
    private ChatSystem app;
    private JLabel label;
    private ControleurChangerPseudo controleurChangerPseudo;

    public ChangerPseudo(ChatSystem app, ControleurChangerPseudo controleurChangerPseudo) {
        super("ChatSystem");
        setContentPane(panel1);
        this.app = app;
        this.controleurChangerPseudo = controleurChangerPseudo;

        OKButton.addActionListener(e -> {
            cmdChangerPseudo();
            dispose();
        });
        textField.addKeyListener(new KeyAdapter() { // Enter KEY dans le textField -> Envoie message et print dans le textArea
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cmdChangerPseudo();
                }
            }
        });
        OKButton.addKeyListener(new KeyAdapter() { // Bouton Creer Session de la frame (ENTER KEY)
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cmdChangerPseudo();
                    dispose();
                }
            }
        });
        cancelButton.addActionListener(e -> dispose());
    }

    public void cmdChangerPseudo(){
        controleurChangerPseudo.actionChangerPseudo(textField.getText());
    }

    public void updateWindow(){
        textField.setText("");
    }

}
