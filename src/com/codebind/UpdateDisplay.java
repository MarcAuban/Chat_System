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
    private JLabel participants;
    private boolean init = false ;

    UpdateDisplay(ChatSystem app, JTextArea textArea, JList listSession, DefaultListModel<String> model, JLabel CurrentSession,JLabel participants){
        this.textArea = textArea;
        this.listSession = listSession;
        this.app = app;
        this.model = model;
        this.sessionList = app.getUser().getSessionList();
        this.CurrentSession = CurrentSession;
        this.participants = participants;
        this.pseudo = null;
    }

    @Override
    public void run() {
        while (true){
            try {
                participants.setText(app.getUsersConnected().toString());

                System.out.println(sessionList);
                if(sessionList.size()>0 && app.getUsersConnected().size()>1) {
                    for (Session s : sessionList) {
                        //System.out.println(s.getParticipants());
                        for (User user : s.getParticipants()) {
                            if(user==null){

                            }
                            else if (!user.getPseudo().equals(app.getUser().getPseudo())) {
                                pseudo = user.getPseudo();
                            }
                        }
                    }
                }
                //System.out.println(sessionList);

                if(!model.contains(pseudo) && pseudo!=null) {
                    model.addElement(pseudo);
                    listSession.setModel(model);
                    listSession.setSelectedIndex(listSession.getLastVisibleIndex());
                    CurrentSession.setText(pseudo);
                    ArrayList<User> UserList = new ArrayList<>();
                    UserList.add(app.getUserFromPseudo(pseudo));
                    UserList.add(app.getUser());
                    session = app.getUser().getSessionFromParticipants(UserList);
                    System.out.println("THREAD" + session.getParticipants());
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
    }
    public void ChangePseudo(String pseudo)
    {
        this.pseudo = pseudo;
    }
    public void showHistory(Session session)
    {
        textArea.setText("");
        for(Message m : session.getHistorique())
        {
            textArea.append(m.toString());
        }
    }
}