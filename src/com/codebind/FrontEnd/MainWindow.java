package com.codebind.FrontEnd;


import com.codebind.BackEnd.ChatSystem;
import com.codebind.BackEnd.Session;
import com.codebind.BackEnd.User;
import com.codebind.Controleur.UpdateDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class MainWindow extends JFrame{
	private JPanel panel1;
	private ChangerPseudo NewPseudo;
	private JList<String> list1;
	private JTextField textField1;
	private JButton creerUneSessionButton;
	private JButton deleteUneSessionButton;
	private JLabel NameUser;
	private JLabel CurrentSession;
	private JLabel UtilisateurConnected;
	private JScrollPane Scroll;
	private JList<String> listUserConnected;
	private JPanel PanelMessage;
	private JTextPane textPane;
	private DefaultListModel<String> model = new DefaultListModel<>();
	private ChatSystem app;
	private Session s;
	private UpdateDisplay updateDisplay;
	private AProposBackground b=null;


	public MainWindow(ChatSystem app, String name) {

		super("ChatSystem");
		this.app = app;
		this.s = null;
		Scroll.setViewportView(textPane);
		Scroll.setWheelScrollingEnabled(true);
		NameUser.setText("<html> Bienvenue "+ name + " <br> </html>");
		Font font1 = new Font("SansSerif", Font.BOLD, 20);
		NameUser.setFont(font1);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE );

		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("Options");
		JMenuItem refresh = new JMenuItem("Rafraichir");
		menu1.add(refresh);
		JMenuItem changerpseudo = new JMenuItem("Changer Pseudo");
		menu1.add(changerpseudo);
		JMenuItem deco = new JMenuItem("Déconnexion");
		menu1.add(deco);
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
		UtilisateurConnected.setText("Utilisateurs connectés : ");

		textField1.setEditable(false);
		setContentPane(panel1);

		if(app.getUser().getPseudo().equals("RGB")){
			b = new AProposBackground(panel1);
			Thread AProposBackgroundThread = new Thread(b);
			AProposBackgroundThread.start();
		}

		addWindowListener(new java.awt.event.WindowAdapter() { //Bouton X de la frame
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(panel1, "Etes vous sur de vouloir fermer l'application ?", "Fermeture de l'application ?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					deconnexionWithOut();
					if(b != null)
						b.stop();
					System.exit(0);
				}
			}
		});

		//Bouton Deconnexion du menu de la frame
		deco.addActionListener(e -> {
			deconnexion();
			dispose();
		});

		aPropos.addActionListener(e -> {
			APropos frame = new APropos();
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		});

		// Bouton Quitter du menu de la frame
		quitter.addActionListener(e -> {
			deconnexionWithOut();
			dispose();
			System.exit(0);
		});

		// Bouton Quitter du menu de la frame
		refresh.addActionListener(e -> app.requestUserList());



		list1.addMouseListener(new MouseAdapter() { // Click on the list1 pour les sessions
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 1) {
					String pseudo = list1.getSelectedValue();
					if(!list1.isSelectionEmpty()) {
						if(!CheckIsUserConnected(pseudo)){
							CurrentSession.setText(list1.getSelectedValue());
							JOptionPane.showMessageDialog(null, "Utilisateur pas connecté", "Erreur", JOptionPane.ERROR_MESSAGE);
							textField1.setEditable(false);
							updateDisplay.ChangeSession(null);
						}
						else {
							if(s!=null) {
								s.setAllMessagesDisplayed(false);
							}
							s = SelectedSession();
							updateDisplay.ChangeSession(s);
							updateDisplay.ChangeIndex();
							CurrentSession.setText(list1.getSelectedValue());
							textField1.setEditable(true);
						}
					}
				}
			}
		});
		list1.addListSelectionListener(e -> {
			s = SelectedSession();
			if(s!=null) {
				s.setAllMessagesDisplayed(false);
				updateDisplay.ChangeSession(s);
				updateDisplay.ChangeIndex();
				CurrentSession.setText(list1.getSelectedValue());
				textField1.setEditable(true);
				//System.out.println("Component added");
			}
		});

		textField1.addKeyListener(new KeyAdapter() { // Enter KEY dans le textField -> Envoie message et print dans le textArea
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!textField1.getText().equals(" ") && CheckIsUserConnected(list1.getSelectedValue())) {
						SendMessage(textField1.getText());
						textField1.setText("");
					}else{
						textField1.setEditable(false);
						textField1.setText("");
						JOptionPane.showMessageDialog(null, "Utilisateur pas connecté", "Erreur", JOptionPane.ERROR_MESSAGE);
						updateDisplay.ChangeSession(null);
					}
				}
			}
		});

		DefaultListModel<String> modelUserConnected = new DefaultListModel<>();
		updateDisplay = new UpdateDisplay(app,Scroll,textPane,textField1,list1,model, CurrentSession, modelUserConnected,listUserConnected);
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
		deleteUneSessionButton.addActionListener(e -> {
			if(model.size()!=0) {
				DeleteSession();
			}
			else{
				JOptionPane.showMessageDialog(null, "Veuillez sélectionner une session à supprimer", "Erreur Supprimer Session", JOptionPane.ERROR_MESSAGE);
			}
		});

		NewPseudo =  new ChangerPseudo(app,NameUser);

		//Bouton ChangerPseudo du menu de la frame
		changerpseudo.addActionListener(e -> {
			NewPseudo.setSize(300,150);
			NewPseudo.setVisible(true);
			NewPseudo.setLocationRelativeTo(null);
		});
		SwingUtilities.invokeLater(()-> {
			JScrollBar scrollBar = Scroll.getVerticalScrollBar();
			scrollBar.setValue(scrollBar.getMaximum());
		});
	}

	public boolean CheckIsUserConnected(String pseudo){
		User checked = app.getUserFromPseudo(pseudo);
		if (checked == null) {
			return false;
		}
		return checked.isOnline();
	}

	public Session SelectedSession()
	{
		if(list1.isSelectionEmpty()){
			return null;
		}
		else
		{
			String pseudo = list1.getSelectedValue();
			ArrayList<User> listUser = new ArrayList<>();
			listUser.add(app.getUserFromPseudo(pseudo));
			listUser.add(app.getUser());

			return app.getUser().getSessionFromParticipants(listUser);
		}

	}
	public void SetNewSession(){
		NewSession newSession = new NewSession(app, model);
		newSession.setSize(500,400);
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
		updateDisplay.deconnexion();
		app.deconnexion();
	}


	public void SendMessage(String message){
		if(!list1.isSelectionEmpty()) {
			s = SelectedSession();
			s.sendMsg(app.getUser(), message);
			//updateDisplay.ChangeSession(s);
		}
	}

	public void DeleteSession(){
		s = SelectedSession();

		updateDisplay.ChangeSession(null);
		updateDisplay.ChangePseudo(null);

		if(s!=null){
			app.getUser().delSession(s);
			model.remove(list1.getSelectedIndex());
		}
		list1.setModel(model);

		CurrentSession.setText("");
		textPane.setText("");
	}
}
