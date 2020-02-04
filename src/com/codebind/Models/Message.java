package com.codebind.Models;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
* Un message envoyé par un utilisateur, il est horodaté
*/
public class Message{
	private SimpleDateFormat dateformat;
	private User sender;
	private String txt;
	private Date date;
	private boolean isDisplayed = false;

	/**
	 * @param sender l'utilisateur qui a envoyé le message
	 * @param txt le message
	 */
	Message(User sender, String txt){
		this.sender=sender;
		this.txt=txt;
		this.date= new Date();
		this.dateformat = new SimpleDateFormat("HH:mm");
	}
	/**
	 * à utiliser que dans la récupération d'une sauvegarde
	 * @param sender l'utilisateur qui a envoyé le message
	 * @param txt le message
	 * @param date la date de l'envoi du message
	 */
	protected Message(User sender, String txt, Date date){
		this.sender=sender;
		this.txt=txt;
		this.date=date;
		this.dateformat = new SimpleDateFormat("HH:mm");
	}

	public String getMessage(){
		return this.txt;
	}

	public User getSender(){
		return this.sender;
	}

	public Date getDate(){
		return this.date;
	}

	public String toString(){
		return this.sender.getPseudo() + "\n" + this.txt + "\n" + this.dateformat.format(this.date) + "\n";
	}

	public void setDisplayed(boolean bool)
	{
		this.isDisplayed = bool;
	}

	public boolean isDisplayed()
	{
		return this.isDisplayed;
	}

}