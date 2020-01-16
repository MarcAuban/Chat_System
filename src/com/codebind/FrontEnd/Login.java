package com.codebind.FrontEnd;
import com.codebind.BackEnd.ChatSystem;
import com.codebind.BackEnd.Receiver;

import javax.swing.*;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Login extends JFrame{
	private JPanel panel1;
	private JTextField textField1;
	private JButton button1;
	private ChatSystem app;

	public Login() {
		super("Login - ChatSystem");
		setContentPane(panel1);

		BlockingQueue<String> queue = new SynchronousQueue<>();

		app = new ChatSystem(new Receiver(queue), queue);
		Thread appThread = new Thread(app);
		appThread.start();

		button1.addActionListener(e -> login());

		button1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					login();
				}
			}
		});

		textField1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					login();
				}
			}
		});


		addWindowListener(new java.awt.event.WindowAdapter() { //Bouton X de la frame
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(panel1, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					deconnexion();
					System.exit(0);
				}
			}
		});
	}

	private void login(){
		if(!app.getUsersConnected().contains(app.getUserFromPseudo(textField1.getText()))) {
			app.login(textField1.getText());
			MainWindow frame = new MainWindow(app, textField1.getText());
			frame.setSize(900, 600);
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			dispose();
		}
		else {
			JOptionPane.showMessageDialog(null, "Pseudo déjà pris", "Erreur pseudo pris", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void deconnexion(){
		app.deconnexion();
	}
}
