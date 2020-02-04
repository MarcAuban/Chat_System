package com.codebind.Controleurs;

import com.codebind.Models.ChatSystem;
import com.codebind.Models.Message;
import com.codebind.Models.Session;
import com.codebind.Models.User;
import com.codebind.Controleurs.ControleurMainWindow;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.lang.Thread;
import java.util.ArrayList;

/**
 * C'est le thread qui recoit les messages,
 * il est automatiquement démarré dans le ChatSystem
 */
public class UpdateDisplay implements Runnable{

    private JTextPane textPane;
    private JList<String> listSession;
	private ArrayList<Session> sessionList;
    private DefaultListModel<String> modelListSession;
    private ChatSystem app;
    private DefaultListModel<String> modelUserConnected;
    private JList<String> listUserConnected;
    private boolean online = true;
    private StyledDocument doc;
    private ControleurMainWindow controleurMainWindow;

    public UpdateDisplay(ChatSystem app, ControleurMainWindow controleurMainWindow, JList<String> listUserConnected, JTextPane textPane, JList<String> listSession){
        this.app = app;
        this.modelUserConnected = new DefaultListModel<>();
        this.listUserConnected = listUserConnected;
        this.textPane = textPane;
        this.listSession = listSession;
        this.controleurMainWindow = controleurMainWindow;
        this.doc = textPane.getStyledDocument();
        this.sessionList = app.getUser().getSessionList();
        this.modelListSession = new DefaultListModel<>();
    }

    @Override
    public void run() {
        while (this.online){
            try {
                listUserConnected.setModel(getModelUserConnected());
                Session currentSession = controleurMainWindow.actionCurrentSession();

                if(currentSession !=null){
                    showHistory(currentSession);
                }
                if(sessionList.size()>0){
                    updateListSession();
                }
                controleurMainWindow.actionSetModelSession(modelListSession);

                if(modelListSession.size()>0) {
                    for(int i = 0 ; i < modelListSession.size() ; i++){
                        ArrayList<User> test = new ArrayList<>();
                        test.add(app.getUserFromPseudo(modelListSession.get(i)));
                        test.add(app.getUser());
                        if(app.getUser().getSessionFromParticipants(test)==null)
                        {
                            modelListSession.remove(i);
                        }
                    }
                }

                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void updateSession(Session session){
        session.setAllMessagesDisplayed(false);
        showHistory(session);
    }
    public void updateListSession(){
        String pseudoContact = "";
        for (Session session : sessionList) {
            if (!session.isDisplayed()){
                modelListSession.clear();
                for(Session s : sessionList){
                    for (User user : s.getParticipants()) {
                        if (user != null && !user.getPseudo().equals(app.getUser().getPseudo())) {
                            pseudoContact = user.getPseudo();
                        }
                    }
                    if (pseudoContact != null) {
                        //System.out.println("UpdateAvant : " + modelListSession);
                        modelListSession.addElement(pseudoContact);
                        //System.out.println("UpdateApres : " + modelListSession);
                        listSession.setModel(modelListSession);
                        listSession.setSelectedIndex(listSession.getLastVisibleIndex());
                        s.setDisplayed(true);
                        showHistory(s);
                    }
                }
            }
        }
    }

    public DefaultListModel<String> getModelUserConnected(){
        modelUserConnected.clear();
        for(User user : app.getUsersConnected()){
            if(!user.getPseudo().equals(app.getUser().getPseudo()) && !modelUserConnected.contains(user.getPseudo())){
                modelUserConnected.addElement(user.getPseudo());
            }
        }
        return modelUserConnected;
    }

    public void setModelListSession(DefaultListModel<String> modelListSession){
        this.modelListSession = modelListSession;
    }

    public void showHistory(Session session) {
        SimpleAttributeSet rightAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(rightAlign,StyleConstants.ALIGN_RIGHT);
        SimpleAttributeSet leftAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(leftAlign,StyleConstants.ALIGN_LEFT);

        if(textPane.getText().equals("")) {
            session.setAllMessagesDisplayed(false);
            for (Message m : session.getHistorique()) {
                int avantMessage = doc.getLength();
                if (!m.isDisplayed()) {
                    try {
                        //Le message nous appartient
                        if (m.getSender().getPseudo().equals(app.getUser().getPseudo())) {
                            doc.insertString(doc.getLength(), m.toString(), rightAlign);
                            doc.setParagraphAttributes(avantMessage, doc.getLength(), rightAlign, false);
                        } else {
                            doc.insertString(doc.getLength(), m.toString(), leftAlign);
                            doc.setParagraphAttributes(avantMessage, doc.getLength(), leftAlign, false);
                        }

                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    m.setDisplayed(true);
                }
            }
        }
        else{
            for (Message m : session.getHistorique()) {
                int avantMessage = doc.getLength();
                if (!m.isDisplayed()) {
                    try {
                        //Le message nous appartient
                        if (m.getSender().getPseudo().equals(app.getUser().getPseudo())) {
                            doc.insertString(doc.getLength(), m.toString(), rightAlign);
                            doc.setParagraphAttributes(avantMessage, doc.getLength(), rightAlign, false);
                        } else {
                            doc.insertString(doc.getLength(), m.toString(), leftAlign);
                            doc.setParagraphAttributes(avantMessage, doc.getLength(), leftAlign, false);
                        }

                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    m.setDisplayed(true);
                }
            }
        }
    }


    public void deconnexion(){
        for(Session session : sessionList)
        {
            session.setAllMessagesDisplayed(false);
        }
        this.online=false;
    }
}