package com.codebind.Controleurs;

import com.codebind.Models.ChatSystem;
import com.codebind.Models.Receiver;
import com.codebind.Models.ModelLogin;
import com.codebind.Views.Login;

import javax.swing.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class ControleurLogin {

    private Login login;
    private ChatSystem app;
    private ModelLogin modelLogin;

    public static void main (String[] args){
        SwingUtilities.invokeLater(() -> {
            try {
                new ControleurLogin();
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public ControleurLogin(){
        BlockingQueue<String> queue = new SynchronousQueue<>();
        app = new ChatSystem(new Receiver(queue), queue);
        Thread appThread = new Thread(app);
        appThread.start();

        login = new Login(this);
        login.setSize(300,300);
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }

    public void clickButtonConnexion(String pseudo){
        modelLogin = new ModelLogin(app, this, pseudo);
        modelLogin.login();
    }

    public void actionDeconnexion(){
        modelLogin = new ModelLogin(app,this);
        modelLogin.deconnexion();
    }

    public void showMainWindow(){
        ControleurMainWindow controleurMainWindow = new ControleurMainWindow(app);
        login.dispose();
    }
}
