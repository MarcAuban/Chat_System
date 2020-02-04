package com.codebind.Models;

import com.codebind.Controleurs.ControleurMainWindow;
import com.codebind.Controleurs.ControleurNewSession;

import javax.swing.*;
import java.util.ArrayList;

public class ModelNewSession {

    private ChatSystem app;
    private ControleurMainWindow controleurMainWindow;
    private ControleurNewSession controleurNewSession;
    private DefaultListModel<String> modelMainWindow;
    private DefaultListModel<String> modelNewSession;
    private JList<String> listUserNewSession;

    public ModelNewSession(ChatSystem app, DefaultListModel<String> modelMainWindow, ControleurMainWindow controleurMainWindow,ControleurNewSession controleurNewSession, JList<String> listUserNewSession){
        this.app = app;
        this.controleurNewSession = controleurNewSession;
        this.controleurMainWindow = controleurMainWindow;
        this.modelMainWindow = modelMainWindow;
        this.modelNewSession = new DefaultListModel<>();
        this.listUserNewSession = listUserNewSession;
        System.out.println(modelMainWindow);
        for(User user : app.getUsersConnected()){
            if(!user.getPseudo().equals(app.getUser().getPseudo()) && !modelMainWindow.contains(user.getPseudo())) {
                modelNewSession.addElement(user.getPseudo());
            }
        }
    }


    public void addNewSession(JList<String> list){
        this.listUserNewSession = list;
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(app.getUserFromPseudo(listUserNewSession.getSelectedValue()));
        userArrayList.add(app.getUser());
        app.getUser().newSession(userArrayList).setDisplayed(true);
        modelMainWindow.addElement(listUserNewSession.getSelectedValue());
        controleurMainWindow.actionUpdateModelSession(modelMainWindow);
        System.out.println("NewSessionModel : " + modelMainWindow);
    }

    public DefaultListModel<String> getModelMainWindow(){
        return modelMainWindow;
    }

    public JList<String> getListUser(){
        return listUserNewSession;
    }

    public DefaultListModel<String> getModelNewSession(){
        return modelNewSession;
    }
}
