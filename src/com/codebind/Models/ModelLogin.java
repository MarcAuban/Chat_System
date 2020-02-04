package com.codebind.Models;

import com.codebind.Controleurs.ControleurLogin;

import javax.swing.*;

public class ModelLogin {

    private ChatSystem app;
    private ControleurLogin controleurLogin;
    private String pseudo;

    public ModelLogin(ChatSystem app, ControleurLogin controleurLogin, String pseudo){
        this.controleurLogin = controleurLogin;
        this.app = app;
        this.pseudo = pseudo;
    }
    public ModelLogin(ChatSystem app, ControleurLogin controleurLogin){
        this.controleurLogin = controleurLogin;
        this.app = app;
        this.pseudo = null;
    }

    public ChatSystem getApp(){
        return this.app;
    }

    public void login(){
        if(!pseudo.equals("")){
            if(!app.getUsersConnected().contains(app.getUserFromPseudo(pseudo))) {
                app.login(pseudo);
                controleurLogin.showMainWindow();
            }
            else {
                JOptionPane.showMessageDialog(null, "Pseudo déjà pris", "Erreur pseudo pris", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Veuillez entrer un pseudo valide", "Pseudo incorrect", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deconnexion(){
        app.deconnexion();
    }
}
