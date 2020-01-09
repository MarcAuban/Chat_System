package com.codebind;

import javax.swing.*;

public class Main {
	public static void main (String[] args){
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new Login();
			frame.setSize(300,300);
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});

	}
}
