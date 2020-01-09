package com.codebind;

import java.util.ArrayList;

/**
* Un utilisateur a un pseudo, une ip et un historique de sessions, son pseudo est unique
*/
public class User{

	private String pseudo;
	private String ip;
	private ArrayList<Session> sessionList = new ArrayList<>();
	private boolean isOnline;

/**
	* @param pseudo le pseudo de l'utilisateur
	* @param ip l'ip locale de la machine sur laquelle est connecté l'utilisateur
	*/
	User(String pseudo, String ip){
		this.pseudo=pseudo;
		this.ip=ip;
		this.isOnline=true;
	}

	@Override
	public String toString(){
		return this.pseudo + "\n" + this.ip;
	}

	protected String getPseudo(){
		return this.pseudo;
	}

	protected void setPseudo(String newPseudo){
		this.pseudo=newPseudo;
	}

	protected String getIP(){
		return this.ip;
	}

	protected ArrayList<Session> getSessionList(){
		return this.sessionList;
	}

/**
	* @param participants une liste d'utilisateurs
	* @return la session qui regroupe ces participants ou une nouvelle session si elle n'existe pas
	*/
	protected Session getSessionFromParticipants(ArrayList<User> participants){
		for(Session session : this.sessionList){
			if(participants.containsAll(session.getParticipants()))
				return session;
		}
		return null;
	}

/**
	* @param session la session à supprimer
	* supprime la session unilatéralement
	*/
	protected void delSession(Session session){
		this.sessionList.remove(session);
	}

	protected Session newSession(ArrayList<User> participants){
		Session session = new Session(participants);
		this.sessionList.add(session);
		return session;
	}

	protected boolean isOnline(){
		return this.isOnline;
	}

	protected void setOnline(boolean online){
		this.isOnline=online;
	}
}