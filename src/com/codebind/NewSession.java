package com.codebind;

import javax.swing.*;
import java.util.ArrayList;

public class NewSession extends JFrame {
    public JList<String> list1;
    private JPanel panel1;
    public JButton cancelButton;
    public JButton OKButton;

    private ChatSystem app;

    public NewSession(ChatSystem app, DefaultListModel<String> modelApp) {

        super("NewSession - ChatSystem");
        this.app = app;

        setContentPane(panel1);

        DefaultListModel<String> model = new DefaultListModel<>();
        for( User user : app.getUsersConnected()){
            if(!user.getPseudo().equals(app.getUser().getPseudo()) && !modelApp.contains(user.getPseudo())) {
                model.addElement(user.getPseudo());
            }
        }

        list1.setModel(model);

        cancelButton.addActionListener(e -> dispose());

        addWindowListener(new java.awt.event.WindowAdapter() { //Bouton X de la frame
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dispose();
            }
        });

        OKButton.addActionListener(e -> {
            if(!list1.isSelectionEmpty()) {
                AjoutSession();
                dispose();
            }
        });
    }

    public void AjoutSession(){
        ArrayList<User> ListUser = new ArrayList<>();
        ListUser.add(app.getUserFromPseudo(list1.getSelectedValue()));
        ListUser.add(app.getUser());
        app.getUser().newSession(ListUser);
    }

}
