package com.codebind.Controleurs;

import com.codebind.Models.ChatSystem;
import com.codebind.Models.Session;
import com.codebind.Models.ModelMainWindow;
import com.codebind.Views.MainWindow;

import javax.swing.*;

public class ControleurMainWindow {

    private ChatSystem app;
    private ModelMainWindow modelMainWindow;
    private MainWindow mainWindow;

    public ControleurMainWindow(ChatSystem app){
        this.app = app;
        mainWindow = new MainWindow(app,this);
        mainWindow.setSize(900, 600);
        mainWindow.setVisible(true);
        mainWindow.setLocationRelativeTo(null);
        modelMainWindow = new ModelMainWindow(app,this,null,mainWindow.getListUserConnected(),mainWindow.getTextPane(),mainWindow.getListSession(),null);
    }

    public void actionDeconnexion(){
        modelMainWindow.endThread();
        modelMainWindow.deconnexion();
        mainWindow.dispose();
        ControleurLogin controleurLogin = new ControleurLogin();
    }

    public void actionQuitter(){
        modelMainWindow.endThread();
        modelMainWindow.deconnexion();
        mainWindow.dispose();
        System.exit(0);
    }

    public void actionRafraichir(){
        modelMainWindow.rafraichir();
    }

    public void actionNewSession(){
        ControleurNewSession controleurNewSession = new ControleurNewSession(app, modelMainWindow.getModelSession(), mainWindow, this);
    }
    public void actionDeleteSession(){
        modelMainWindow.deleteSession();
    }
    public void actionSetModelSession(DefaultListModel<String> modelListSession){
        modelMainWindow.setModelSession(modelListSession);
    }

    public void actionIndexChange(){
        modelMainWindow.selectedIndex(mainWindow.getListSession(),mainWindow.getTextField());
        mainWindow.updateSelectedIndex();
    }

    public void actionSendMessage(String message){
        modelMainWindow.sendMessage(message);
    }

    public void actionUpdateTchat(){
        mainWindow.updateTchat();
    }

    public void actionUpdateModelSession(DefaultListModel<String> model){
        modelMainWindow.setModelSession(model);
    }
    public void actionClearPane(){
        mainWindow.clearPane();
    }
    public void actionUpdateTchatDeco(){
        mainWindow.updateTchatDeco();
    }
    public void actionChangerPseudo(){
        ControleurChangerPseudo controleurChangerPseudo = new ControleurChangerPseudo(app, this,modelMainWindow.getModelSession());
    }
    public void actionUpdatePseudo(){
        mainWindow.updatePseudo();
    }

    public Session actionCurrentSession(){
        return modelMainWindow.getCurrentSession();
    }

    public JTextPane actionGetTextPane(){
        return mainWindow.getTextPane();
    }

    public DefaultListModel<String> actionGetModelListSession(){
        System.out.println(modelMainWindow);
        return modelMainWindow.getModelSession();
    }

    public JList<String> actionGetListSession(){
        return mainWindow.getListSession();
    }

    public JList<String> actionGetListUserConnected(){
        return mainWindow.getListUserConnected();
    }

    public void actionUpdatePseudoThread(){
        System.out.println("Controleur Mainwwindow ");
        modelMainWindow.updatePseudoThread();
    }
}
