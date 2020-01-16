package com.codebind.FrontEnd;

import com.codebind.BackEnd.ChatSystem;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChangerPseudo extends JFrame{
    private JButton OKButton;
    private JTextField textField;
    private JPanel panel1;
    private JButton cancelButton;
    private ChatSystem app;
    private JLabel NameUser;
    private JLabel label;

    public ChangerPseudo(ChatSystem app,JLabel NameUser) {
        super("ChatSystem");
        setContentPane(panel1);
        this.app = app;
        this.NameUser = NameUser;

        OKButton.addActionListener(e -> {
            UpdatePseudo();
            textField.setText("");
            dispose();
        });
        textField.addKeyListener(new KeyAdapter() { // Enter KEY dans le textField -> Envoie message et print dans le textArea
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(!textField.getText().equals(" ")) {
                        UpdatePseudo();
                        textField.setText("");
                        dispose();
                    }
                }
            }
        });
        OKButton.addKeyListener(new KeyAdapter() { // Bouton Creer Session de la frame (ENTER KEY)
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    UpdatePseudo();
                    textField.setText("");
                    dispose();
                }
            }
        });
        cancelButton.addActionListener(e -> dispose());
    }

    private void changerPseudo(String newpseudo){
        if(!app.changerPseudo(newpseudo))
        { JOptionPane.showMessageDialog(
                        null, "Pseudo déjà pris", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void UpdatePseudo()
    {
        String name = textField.getText();
        changerPseudo(name);
        NameUser.setText("<html> Bienvenue "+ app.getUser().getPseudo() + " <br> </html>");
    }
}
