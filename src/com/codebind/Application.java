package com.codebind;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JFrame;


public class Application extends JFrame{
	private JPanel panel1;
	private ChangerPseudo NewPseudo;
	private JList<String> list1;
	private JTextField textField1;
	private JTextArea textArea1;
	private JButton creerUneSessionButton;
	private JButton deleteUneSessionButton;
	private JLabel NameUser;
	private JLabel CurrentSession;
	private JLabel UtilisateurConnected;
	private JScrollPane Scroll;
	private JList listUserConnected;
	private DefaultListModel<String> model = new DefaultListModel<>();
	private DefaultListModel<String> modelUserConnected = new DefaultListModel<>();
	private ChatSystem app;
	private Session s;
	private UpdateDisplay updateDisplay;


	public Application(ChatSystem app,String name) {

		super("ChatSystem");
		this.app = app;
		this.s = null;

		Scroll.setViewportView(textArea1);
		Scroll.setWheelScrollingEnabled(true);

		NameUser.setText("<html> Bienvenue "+ name + " <br> </html>");
		Font font1 = new Font("SansSerif", Font.BOLD, 20);
		NameUser.setFont(font1);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE );

		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("Options");
		JMenuItem changerpseudo = new JMenuItem("Changer Pseudo");
		menu1.add(changerpseudo);
		JMenuItem deco = new JMenuItem("Déconnexion");
		menu1.add(deco);
		JMenuItem refresh = new JMenuItem("Rafraichir");
		menu1.add(refresh);
		JMenuItem quitter = new JMenuItem("Quitter");
		menu1.add(quitter);
		menuBar.add(menu1);
		JMenu menu2 = new JMenu("A propos");
		JMenuItem aPropos = new JMenuItem("A propos");
		menu2.add(aPropos);
		menuBar.add(menu2);
		setJMenuBar(menuBar);
		model.clear();
		listUserConnected.setFocusable(false);
		listUserConnected.setOpaque(false);
		UtilisateurConnected.setText("Utilisateur en ligne : ");

		textField1.setEditable(true);
		setContentPane(panel1);

		addWindowListener(new java.awt.event.WindowAdapter() { //Bouton X de la frame
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(panel1, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					deconnexionWithOut();
					System.exit(0);
				}
			}
		});

		//Bouton Deconnexion du menu de la frame
		deco.addActionListener(e -> {
			deconnexion();
			dispose();
		});

		// Bouton Quitter du menu de la frame
		quitter.addActionListener(e -> {
			deconnexionWithOut();
			dispose();
		});

		// Bouton Quitter du menu de la frame
		refresh.addActionListener(e -> getUserList());

		list1.addMouseListener(new MouseAdapter() { // Click on the list1 pour les sessions
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 1) {
					String pseudo = list1.getSelectedValue();
					if(!list1.isSelectionEmpty()) {
						if(!CheckIsUserConnected(pseudo)){
							textField1.setEditable(false);
							s = SelectedSession();
							CurrentSession.setText(list1.getSelectedValue());
							updateDisplay.ChangeSession(s);
							updateDisplay.showHistory(s);
							JOptionPane.showMessageDialog(null, "Utilisateur pas connecté", "Erreur", JOptionPane.ERROR_MESSAGE);
						}
						else {
							if(s!=null) {
								s.setAllMessagesDisplayed(false);
							}
							s = SelectedSession();
							updateDisplay.ChangeSession(s);
							CurrentSession.setText(list1.getSelectedValue());
							textField1.setEditable(true);
						}
					}
				}
			}
		});


		textField1.addKeyListener(new KeyAdapter() { // Enter KEY dans le textField -> Envoie message et print dans le textArea
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!textField1.getText().equals(" ")) {
						SendMessage(textField1.getText());
						textField1.setText("");
					}
				}
			}
		});



		updateDisplay = new UpdateDisplay(app,textArea1,list1,model, CurrentSession,modelUserConnected,listUserConnected);
		Thread updatehistoryThread = new Thread(updateDisplay);
		updatehistoryThread.start();


		creerUneSessionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				SetNewSession();
			}
		});

		creerUneSessionButton.addKeyListener(new KeyAdapter() { // Bouton Creer Session de la frame (ENTER KEY)
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					SetNewSession();
				}
			}
		});

		// Bouton Delete une session de la frame
		deleteUneSessionButton.addActionListener(e -> DeleteSession());

		NewPseudo =  new ChangerPseudo(app,NameUser);

		//Bouton ChangerPseudo du menu de la frame
		changerpseudo.addActionListener(e -> {
			NewPseudo.setSize(400,250);
			NewPseudo.setVisible(true);
			NewPseudo.setLocationRelativeTo(null);
		});
	}


	public boolean CheckIsUserConnected(String pseudo){
		return app.getUserFromPseudo(pseudo)!=null;
	}

	public void getUserList(){
		app.requestUserList();
	}

	public Session SelectedSession()
	{
		String pseudo = list1.getSelectedValue();
		ArrayList<User> listUser = new ArrayList<>();
		listUser.add(app.getUserFromPseudo(pseudo));
		listUser.add(app.getUser());
		return app.getUser().getSessionFromParticipants(listUser);
	}
	public void SetNewSession(){
		NewSession newSession = new NewSession(app, model, CurrentSession, list1);
		newSession.setSize(400,400);
		newSession.setVisible(true);
		newSession.setLocationRelativeTo(null);
		textField1.setEditable(true);
	}

	public void deconnexion(){
		app.deconnexion();
		updateDisplay.deconnexion();
		JFrame frame = new Login();
		frame.setSize(300,300);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	public void deconnexionWithOut(){
		app.deconnexion();
	}


	public void SendMessage(String message){
		if(!list1.isSelectionEmpty()) {
			s = SelectedSession();
			s.sendMsg(app.getUser(), message);
			//updateDisplay.ChangeSession(s);
		}
	}

	public ArrayList<User> AddUserToArray(User user){
		ArrayList<User> participantsSession = new ArrayList<>();
		participantsSession.add(user);
		participantsSession.add(app.getUser());
		return participantsSession;
	}

	public void DeleteSession(){
		User user = app.getUserFromPseudo(list1.getSelectedValue());
		s = app.getUser().getSessionFromParticipants(AddUserToArray(user));
		app.getUser().delSession(s);

		updateDisplay.ChangeSession(null);
		updateDisplay.ChangePseudo(null);

		model.remove(list1.getSelectedIndex());
		list1.setModel(model);

		CurrentSession.setText("");
		textArea1.setText("");
	}
}
