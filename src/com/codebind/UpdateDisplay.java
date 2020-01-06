package com.codebind;

import javax.swing.*;
import java.lang.Thread;
import java.util.ArrayList;

/**
 * C'est le thread qui recoit les messages,
 * il est automatiquement démarré dans le ChatSystem
 */
public class UpdateDisplay implements Runnable{

    private JTextArea textArea;
    private JList listSession;
	private ArrayList<Session> sessionList;
    private DefaultListModel<String> model;
    private ChatSystem app;
    private String pseudo;
    private Session session;
    private JLabel CurrentSession;
    private boolean init = false ;
    private DefaultListModel<String> modelUserConnected;
    private JList listUserConnected;
    private boolean online = true;

    UpdateDisplay(ChatSystem app, JTextArea textArea, JList listSession, DefaultListModel<String> model, JLabel CurrentSession,DefaultListModel<String> modelUserconnected, JList listUserConnected){
        this.textArea = textArea;
        this.listSession = listSession;
        this.app = app;
        this.model = model;
        this.sessionList = app.getUser().getSessionList();
        this.CurrentSession = CurrentSession;
        this.pseudo = null;
        this.modelUserConnected = modelUserconnected;
        this.listUserConnected = listUserConnected;
    }

    @Override
    public void run() {
        while (online){
            try {
                modelUserConnected.clear();
                for(User user : app.getUsersConnected()){
                    if(!user.getPseudo().equals(app.getUser().getPseudo()) && !modelUserConnected.contains(user.getPseudo())){
                        modelUserConnected.addElement(user.getPseudo());
                    }
                }
                listUserConnected.setModel(modelUserConnected);

                System.out.println(sessionList);
                if(sessionList.size()>0 && app.getUsersConnected().size()>1) {
                    for (Session s : sessionList) {
                        for (User user : s.getParticipants()) {
                            if (user!=null && !user.getPseudo().equals(app.getUser().getPseudo())) {
                                pseudo = user.getPseudo();
                            }
                        }
                        if(pseudo!=null && !s.isDisplayed()) {
                            s.setDisplayed();
                            model.addElement(pseudo);
                            listSession.setModel(model);
                            listSession.setSelectedIndex(listSession.getLastVisibleIndex());
                            CurrentSession.setText(pseudo);
                        }
                    }
                }

                if(session!=null) {
                    showHistory(session);
                }

                if(!init)
                {
                    initialisation();
                }
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void initialisation(){
        if(model.size()>0){
            listSession.setSelectedIndex(listSession.getLastVisibleIndex()+1);
            System.out.println(model.toString());
            init = true;
            System.out.println("Initialisation  " + listSession.getLastVisibleIndex());
        }

    }
    public void ChangeSession(Session session)
    {
        this.session = session;
        wipeHistory();
    }
    public void ChangePseudo(String pseudo)
    {
        this.pseudo = pseudo;
    }
    public void showHistory(Session session)
    {
        for(Message m : session.getHistorique())
        {
            if(!m.isDisplayed()) {
                textArea.append(m.toString());
                m.setDisplayed(true);
            }
        }
    }
    public void wipeHistory(){
        textArea.setText("");
    }

    public boolean deconnexion(){
        return !online;
    }
}