package com.codebind.Views;

import com.codebind.Models.ChatSystem;
import com.codebind.Controleurs.ControleurNewSession;

import javax.swing.*;

public class NewSession extends JFrame {
    private JList<String> listUserNewSession;
    private JPanel panel1;
    private JButton cancelButton;
    private JButton OKButton;
    private ChatSystem app;
    private ControleurNewSession controleurNewSession;

    public NewSession(ChatSystem app, ControleurNewSession controleurNewSession) {

        super("NewSession - ChatSystem");
        this.app = app;
        this.controleurNewSession = controleurNewSession;

        setContentPane(panel1);

        cancelButton.addActionListener(e -> cmdCloseWindow());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                cmdCloseWindow();
            }
        });

        OKButton.addActionListener(e -> {
            if(!listUserNewSession.isSelectionEmpty()) {
                cmdAddNewSession();
                cmdCloseWindow();
            }
        });
    }

    private void cmdAddNewSession(){
        controleurNewSession.clickAjoutSession(listUserNewSession);
    }

    private void cmdCloseWindow(){
        controleurNewSession.closeWindow();
    }

    public void updateList(DefaultListModel<String> model){
        listUserNewSession.setModel(model);
    }
}
