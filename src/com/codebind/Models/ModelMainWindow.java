package com.codebind.Models;

import com.codebind.Controleurs.ControleurMainWindow;
import com.codebind.Controleurs.UpdateDisplay;

import javax.swing.*;
import java.util.ArrayList;

public class ModelMainWindow {

    private ChatSystem app;
    private ControleurMainWindow controleurMainWindow;
    private DefaultListModel<String> modelSession;
    private JList<String> listSessionMainWindow;
    private JList<String>  listUserConnected;
    private UpdateDisplay updateDisplay;
    private JTextPane textPane;

    public ModelMainWindow(ChatSystem app, ControleurMainWindow controleurMainWindow, DefaultListModel<String> modelUserConnected, JList<String> listUserConnected,JTextPane textPane,JList<String> listSessionMainWindow, DefaultListModel<String> modelSession){
        this.app = app;
        this.controleurMainWindow = controleurMainWindow;
        this.listUserConnected = listUserConnected;
        this.textPane = textPane;
        this.listSessionMainWindow = listSessionMainWindow;
        this.modelSession = modelSession;

        updateDisplay = new UpdateDisplay(app,controleurMainWindow,listUserConnected,textPane,listSessionMainWindow);
        Thread updatehistoryThread = new Thread(updateDisplay);
        updatehistoryThread.start();
    }

    public void deconnexion() {
        app.deconnexion();
    }

    public void endThread(){
        updateDisplay.deconnexion();
    }
    public void rafraichir(){
        app.requestUserList();
    }

    public DefaultListModel<String> getModelSession(){
        return modelSession;
    }

    public void setModelSession(DefaultListModel<String> modelSession){
        this.modelSession = modelSession;
        updateDisplay.setModelListSession(modelSession);
    }

    public Session getCurrentSession(){
        if(listSessionMainWindow!=null) {
            if (listSessionMainWindow.isSelectionEmpty()) {
                return null;
            } else {
                String pseudo = listSessionMainWindow.getSelectedValue();
                ArrayList<User> listUser = new ArrayList<>();
                listUser.add(app.getUserFromPseudo(pseudo));
                listUser.add(app.getUser());
                return app.getUser().getSessionFromParticipants(listUser);
            }
        }
        return null;
    }
    public void selectedIndex(JList<String> listSessionMainWindow, JTextField textField){
        this.listSessionMainWindow = listSessionMainWindow;
        String pseudo = listSessionMainWindow.getSelectedValue();
        if(!listSessionMainWindow.isSelectionEmpty()) {
            if(!CheckIsUserConnected(pseudo)){
                JOptionPane.showMessageDialog(null, "Utilisateur pas connecté", "Erreur", JOptionPane.ERROR_MESSAGE);
                textField.setEditable(false);
            }
            else {
                Session session = getCurrentSession();
                textField.setEditable(true);
                updateDisplay.updateSession(session);
            }
        }

    }

    private boolean CheckIsUserConnected(String pseudo){
        User checked = app.getUserFromPseudo(pseudo);
        if (checked == null) {
            return false;
        }
        return checked.isOnline();
    }

    public void sendMessage(String message){
        Session session = getCurrentSession();
        if(!message.equals(" ") && CheckIsUserConnected(listSessionMainWindow.getSelectedValue())){
            if(!listSessionMainWindow.isSelectionEmpty()) {
                if(session!=null) {
                    session.sendMsg(app.getUser(), message);
                    controleurMainWindow.actionUpdateTchat();
                }
            }
        }
        else{
            controleurMainWindow.actionUpdateTchatDeco();
            JOptionPane.showMessageDialog(null, "Utilisateur pas connecté", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteSession(){
        if(modelSession.size()!=0) {
            Session session = getCurrentSession();
            if(session!=null){
                app.getUser().delSession(session);
                modelSession.remove(listSessionMainWindow.getSelectedIndex());
            }
            listSessionMainWindow.setModel(modelSession);
            controleurMainWindow.actionClearPane();
        }
        else{
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une session à supprimer", "Erreur Supprimer Session", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void updatePseudoThread(){
        updateDisplay.updateListSession();
    }
}
