package com.codebind.Models;

import com.codebind.Controleurs.ControleurChangerPseudo;

import javax.swing.*;

public class ModelChangerPseudo {

    private ChatSystem app;
    private ControleurChangerPseudo controleurChangerPseudo;

    public ModelChangerPseudo(ChatSystem app, ControleurChangerPseudo controleurChangerPseudo){
        this.app = app;
        this.controleurChangerPseudo = controleurChangerPseudo;
    }

    public void changerPseudo(String newpseudo){
        if(!newpseudo.equals(" ")){
            if(!app.changerPseudo(newpseudo))
            {
                JOptionPane.showMessageDialog(null, "Pseudo déjà pris", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
