package com.codebind.Controleurs;

import com.codebind.Models.ChatSystem;
import com.codebind.Models.ModelChangerPseudo;
import com.codebind.Views.ChangerPseudo;

import javax.swing.*;

public class ControleurChangerPseudo {

    private ChatSystem app;
    private ChangerPseudo changerPseudo;
    private ModelChangerPseudo modelChangerPseudo;
    private ControleurMainWindow controleurMainWindow;
    private DefaultListModel<String> modelSession;

    public ControleurChangerPseudo(ChatSystem app, ControleurMainWindow controleurMainWindow,DefaultListModel<String> modelSession){
        this.app = app;
        this.controleurMainWindow = controleurMainWindow;
        this.modelSession = modelSession;
        changerPseudo = new ChangerPseudo(app,this);
        changerPseudo.setSize(300,150);
        changerPseudo.setVisible(true);
        changerPseudo.setLocationRelativeTo(null);
    }

    public void actionChangerPseudo(String newpseudo){
        modelChangerPseudo = new ModelChangerPseudo(app ,this);
        modelChangerPseudo.changerPseudo(newpseudo);
        controleurMainWindow.actionUpdatePseudo();
        controleurMainWindow.actionUpdatePseudoThread();
        System.out.println("ChangerPseudo Controleuir");
        changerPseudo.dispose();
    }
}
