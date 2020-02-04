package com.codebind.Controleurs;

import com.codebind.Models.ChatSystem;
import com.codebind.Models.ModelNewSession;
import com.codebind.Views.MainWindow;
import com.codebind.Views.NewSession;

import javax.swing.*;

public class ControleurNewSession {

    private NewSession newSession;
    private ModelNewSession modelNewSession;
    private JList<String> listUserNewSession;
    private MainWindow mainWindow;

    public ControleurNewSession(ChatSystem app, DefaultListModel<String> modelListMainWindow,MainWindow mainWindow, ControleurMainWindow controleurMainWindow){
        this.mainWindow = mainWindow;
        newSession = new NewSession(app,this);
        newSession.setSize(500,400);
        newSession.setVisible(true);
        newSession.setLocationRelativeTo(null);
        modelNewSession = new ModelNewSession(app,modelListMainWindow,controleurMainWindow,this,listUserNewSession);
        newSession.updateList(modelNewSession.getModelNewSession());
    }

    public void clickAjoutSession(JList<String> list){
        modelNewSession.addNewSession(list);
        mainWindow.updateListSession(modelNewSession.getModelMainWindow());
    }

    public void closeWindow(){
        newSession.dispose();
    }
}
