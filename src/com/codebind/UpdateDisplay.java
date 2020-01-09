package com.codebind;

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
    private DefaultListModel<String> model;
    private ChatSystem app;
    private String pseudo;
    private Session session;
    private JLabel CurrentSession;
    private boolean init = false;
    private DefaultListModel<String> modelUserConnected;
    private JList<String> listUserConnected;
    private boolean online = true;
    private JTextField textField;
    private JScrollPane Scroll;
    private StyledDocument doc;

    UpdateDisplay(ChatSystem app, JScrollPane Scroll , JTextPane textPane, JTextField textField , JList<String> listSession, DefaultListModel<String> model, JLabel CurrentSession, DefaultListModel<String> modelUserconnected, JList<String> listUserConnected){
        this.textPane = textPane;
        this.textField = textField;
        this.doc = textPane.getStyledDocument();
        this.listSession = listSession;
        this.app = app;
        this.model = model;
        this.sessionList = app.getUser().getSessionList();
        this.CurrentSession = CurrentSession;
        this.pseudo = null;
        this.modelUserConnected = modelUserconnected;
        this.listUserConnected = listUserConnected;
        this.Scroll = Scroll;
    }

    @Override
    public void run() {
        while (this.online){
            try {
                modelUserConnected.clear();
                for(User user : app.getUsersConnected()){
                    if(!user.getPseudo().equals(app.getUser().getPseudo()) && !modelUserConnected.contains(user.getPseudo())){
                        modelUserConnected.addElement(user.getPseudo());
                    }
                }

                listUserConnected.setModel(modelUserConnected);

                if(sessionList.size()>0 && app.getUsersConnected().size()>1) {
                    for (Session s : sessionList) {
                        for (User user : s.getParticipants()) {
                            if (user!=null && !user.getPseudo().equals(app.getUser().getPseudo())) {
                                //ne séléctionne qu'un seul pseudo au lieu de plusieurs
                                pseudo = user.getPseudo();
                            }
                        }
                        if(pseudo!=null && !s.isDisplayed()) {
                            s.setDisplayed(true);
                            model.addElement(pseudo);
                            listSession.setModel(model);
                            listSession.setSelectedIndex(listSession.getLastVisibleIndex());
                            CurrentSession.setText(pseudo);
                        }
                    }
                }

                if(session!=null) {
                    textField.setEditable(true);
                    showHistory(session);
                }
                else{
                    textField.setEditable(false);
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
        }
    }

    public void ChangeSession(Session session)
    {
        if(this.session!=null) {
            this.session.setAllMessagesDisplayed(false);
        }
        this.session = session;
        wipeHistory();
    }
    public void ChangeIndex(){
        textPane.setText("");
    }
    
    public void ChangePseudo(String pseudo)
    {
        this.pseudo = pseudo;
    }
    public void showHistory(Session session)
    {

        SimpleAttributeSet rightAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(rightAlign,StyleConstants.ALIGN_RIGHT);
        SimpleAttributeSet leftAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(leftAlign,StyleConstants.ALIGN_LEFT);

        for(Message m : session.getHistorique())
        {
            int avantMessage = doc.getLength();
            if(!m.isDisplayed()) {
                try {
                    //Le message nous appartient
                    if(m.getSender().getPseudo().equals(app.getUser().getPseudo())) {
                        doc.insertString(doc.getLength(),m.toString(),rightAlign);
                        doc.setParagraphAttributes(avantMessage,doc.getLength(),rightAlign,false);
                    }
                    else{
                        doc.insertString(doc.getLength(),m.toString(),leftAlign);
                        doc.setParagraphAttributes(avantMessage,doc.getLength(),leftAlign,false);
                    }

                }
                catch (BadLocationException e) {
                    e.printStackTrace();
                }
                m.setDisplayed(true);

                JScrollBar scrollBar = Scroll.getVerticalScrollBar();
                scrollBar.setValue(scrollBar.getMaximum());
            }
        }
    }
    public void wipeHistory(){
        textPane.setText("");
    }

    public void deconnexion(){
        this.online=false;
    }
}