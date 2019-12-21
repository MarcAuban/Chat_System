package com.codebind;
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
	public ChatSystem app;
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
		app.login(textField1.getText());
		//app.addConnectedUser(new User("Morini","192.168.1.1"));
		//app.addConnectedUser(new User("Jorini", "192.168.43.1"));
		Application frame = new Application(app,textField1.getText());
		frame.setSize(900,600);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		dispose();
	}

	public void deconnexion(){
		app.deconnexion();
	}
}
