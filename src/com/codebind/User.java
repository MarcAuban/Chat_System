package com.codebind;

import java.util.ArrayList;
import java.util.Collection;

/**
* Un utilisateur a un pseudo, une ip et un historique de sessions, son pseudo est unique
*/
public class User{

	private String pseudo;
	private String ip;
	private ArrayList<Session> sessionList = new ArrayList<>();

/**
	* @param pseudo le pseudo de l'utilisateur
	* @param ip l'ip locale de la machine sur laquelle est connecté l'utilisateur
	*/
	User(String pseudo, String ip){
		this.pseudo=pseudo;
		this.ip=ip;
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
			for (User u : session.getParticipants())
				System.out.println(u.hashCode());
			for (User u : participants)
				System.out.println(u.hashCode());
			System.out.println("participants session : " + session.getParticipants());
			System.out.println("liste de marc : "+ participants);
			if(participants.containsAll(session.getParticipants()))
				return session;
		}
		System.out.println("nulllllle");
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
}