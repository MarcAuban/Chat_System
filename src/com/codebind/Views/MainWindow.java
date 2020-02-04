package com.codebind.Views;


import com.codebind.Controleurs.AProposBackground;
import com.codebind.Models.ChatSystem;
import com.codebind.Controleurs.ControleurMainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainWindow extends JFrame{

	private JPanel panel1;
	private JList<String> list1;
	private JTextField textField;
	private JButton creerUneSessionButton;
	private JButton deleteUneSessionButton;
	private JLabel NameUser;
	private JLabel CurrentSession;
	private JLabel UtilisateurConnected;
	private JScrollPane Scroll;
	private JList<String> listUserConnected;
	private JPanel PanelMessage;
	private JTextPane textPane;
	private ChatSystem app;
	private AProposBackground b=null;


	private ControleurMainWindow controleurMainWindow;


	public MainWindow(ChatSystem app, ControleurMainWindow controleurMainWindow) {

		super("ChatSystem");
		this.controleurMainWindow = controleurMainWindow;
		this.app = app;
		Scroll.setViewportView(textPane);
		Scroll.setWheelScrollingEnabled(true);
		NameUser.setText("<html> Bienvenue "+ app.getUser().getPseudo() + " <br> </html>");
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
		listUserConnected.setFocusable(false);
		listUserConnected.setOpaque(false);
		UtilisateurConnected.setText("Utilisateurs connectés : ");

		textField.setEditable(false);
		setContentPane(panel1);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		deco.addActionListener(e -> cmdDeconnexion());
		quitter.addActionListener(e -> cmdQuitter());

		refresh.addActionListener(e -> cmdRafraichir());
		creerUneSessionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				cmdCreerSession();
			}
		});

		creerUneSessionButton.addKeyListener(new KeyAdapter() { // Bouton Creer Session de la frame (ENTER KEY)
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					cmdCreerSession();
				}
			}
		});



		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



		if(app.getUser().getPseudo().equals("RGB")){
			b = new AProposBackground(panel1);
			Thread AProposBackgroundThread = new Thread(b);
			AProposBackgroundThread.start();
		}

		addWindowListener(new java.awt.event.WindowAdapter() { //Bouton X de la frame
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(panel1, "Etes vous sur de vouloir fermer l'application ?", "Fermeture de l'application ?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					cmdQuitter();
					if(b != null)
						b.stop();
					System.exit(0);
				}
			}
		});


		aPropos.addActionListener(e -> {
			APropos frame = new APropos();
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		});




		list1.addMouseListener(new MouseAdapter() { // Click on the list1 pour les sessions
			@Override
		   	public void mouseClicked(MouseEvent e) {
			   	super.mouseClicked(e);
			   	if (e.getClickCount() == 1) {
				   	cmdIndexSessionChange();
			   	}
		   	}
	    });

		list1.addListSelectionListener(e -> cmdIndexSessionChange());

		textField.addKeyListener(new KeyAdapter() { // Enter KEY dans le textField -> Envoie message et print dans le textArea
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					cmdSendMessage();
				}
			}
		});
/*
		*/


		// Bouton Delete une session de la frame
		deleteUneSessionButton.addActionListener(e -> cmdDeleteSession());

		//Bouton ChangerPseudo du menu de la frame
		changerpseudo.addActionListener(e -> cmdChangerPseudo());
		SwingUtilities.invokeLater(()-> {
			JScrollBar scrollBar = Scroll.getVerticalScrollBar();
			scrollBar.setValue(scrollBar.getMaximum());
		});
	}


	public void cmdCreerSession(){
		controleurMainWindow.actionNewSession();
	}

	public void cmdDeleteSession(){
		controleurMainWindow.actionDeleteSession();
	}

	public void cmdChangerPseudo(){
		controleurMainWindow.actionChangerPseudo();
	}

	public void cmdDeconnexion(){
		controleurMainWindow.actionDeconnexion();
	}

	public void cmdQuitter(){
		controleurMainWindow.actionQuitter();
	}

	public void cmdRafraichir(){
		controleurMainWindow.actionRafraichir();
	}

	public JList<String> getListSession(){
		return list1;
	}
	public JList<String> getListUserConnected(){
		return listUserConnected;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void clearPane(){
		textPane.setText("");
	}
	public JTextPane getTextPane(){
		return textPane;
	}

	public void updateListSession(DefaultListModel<String> model){
		list1.setModel(model);
		list1.setSelectedIndex(list1.getLastVisibleIndex());
	}
	public void cmdIndexSessionChange(){
		controleurMainWindow.actionIndexChange();
	}
	public void updateSelectedIndex(){
		CurrentSession.setText(list1.getSelectedValue());
		textPane.setText("");
	}
	public void cmdSendMessage(){
		controleurMainWindow.actionSendMessage(textField.getText());
	}

	public void updatePseudo(){
		NameUser.setText("<html> Bienvenue "+ app.getUser().getPseudo() + " <br> </html>");
	}
	public void updateTchat(){
		textField.setText("");
	}
	public void updateTchatDeco(){
		textField.setEditable(false);
		textField.setText("");
	}
}
