package com.codebind.Views;
import com.codebind.Controleurs.ControleurLogin;

import javax.swing.*;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame{
	private JPanel panel1;
	private JTextField textField;
	private JButton Connexion;
	private ControleurLogin controleurLogin;

	public Login(ControleurLogin controleurLogin) {
		super("Login - ChatSystem");
		this.controleurLogin = controleurLogin;

		setContentPane(panel1);

		Connexion.addActionListener(e -> cmdConnexion());

		Connexion.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					cmdConnexion();
				}
			}
		});

		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					cmdConnexion();
				}
			}
		});

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(panel1, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					cmdDeconnexion();
					System.exit(0);
				}
			}
		});
	}

	private void cmdConnexion(){
		//Solicité le controleur qui va lui même soliciter le modele pour faire les calculs
		//retour par une méthode pour afficher
		controleurLogin.clickButtonConnexion(textField.getText());
	}

	private void cmdDeconnexion(){
		controleurLogin.actionDeconnexion();
	}
}
