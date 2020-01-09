package com.codebind;

import java.util.ArrayList;

/**
* Une session regroupe des participants et seuvegarde un historique des messages
*/
public class Session{

	private ArrayList<Message> historique = new ArrayList<>();
	private ArrayList<User> participants;
	private boolean isDisplayed = false;

/**
	* @param participants la liste des utilisateurs qui sont dans cette session
	*/
	Session(ArrayList<User> participants){
		this.participants=participants;
	}

	protected ArrayList<Message> getHistorique(){
		return this.historique;
	}

	protected ArrayList<User> getParticipants(){
		return this.participants;
	}

	protected void setHistorique(ArrayList<Message> historique){
		this.historique=historique;
	}

	/**
	* @param sender l'utilisateur qui a envoyé le message
	* @param txt le message
	*/
	protected void receivedMsg(User sender, String txt){
		Message m = new Message(sender, txt);
		this.historique.add(m);
		//à faire : update l'UI*/
	}

/**
	* @param sender l'utilisateur qui va envoyer le message
	* @param txt le message
	*/
	protected void sendMsg(User sender, String txt){
		StringBuilder header = new StringBuilder("message\n" + sender.toString() + "\n" + this.participants.size() + "\n");
		for(User user : this.participants)
			header.append(user.toString()).append("\n");
		header.append("end\n");

		for(User user : this.participants){
			if(!user.equals(sender)){//évite l'envoi d'un message à soi même
				Sender.send(user.getIP(), header + txt);
			}
		}

		this.historique.add(new Message(sender, txt));
	}

	public void setDisplayed(boolean bool)
	{
		this.isDisplayed = bool;
	}

	public boolean isDisplayed()
	{
		return this.isDisplayed;
	}

	public void setAllMessagesDisplayed(boolean bool){
		for(Message m : this.historique){
			m.setDisplayed(bool);
		}
	}
}