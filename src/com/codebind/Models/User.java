package com.codebind.Models;

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

	public String getPseudo(){
		return this.pseudo;
	}

	protected void setPseudo(String newPseudo){
		this.pseudo=newPseudo;
	}

	protected String getIP(){
		return this.ip;
	}

	public ArrayList<Session> getSessionList(){
		/*
		for(Session s : this.sessionList){
			for(User u : s.getParticipants()){
				System.out.println(u.toString());
			}
		}
		*/
		return this.sessionList;
	}

/**
	* @param participants une liste d'utilisateurs
	* @return la session qui regroupe ces participants ou une nouvelle session si elle n'existe pas
	*/
	public Session getSessionFromParticipants(ArrayList<User> participants){
		for(Session session : this.sessionList){
			/*
			for (User u : session.getParticipants())
				System.out.println(u.hashCode());
			for (User u : participants)
				System.out.println(u.hashCode());
			System.out.println("participants session : " + session.getParticipants());
			System.out.println("liste de marc : "+ participants);
			*/
			if(participants.containsAll(session.getParticipants()))
				return session;
		}
		return null;
	}

/**
	* @param session la session à supprimer
	* supprime la session unilatéralement
	*/
	public void delSession(Session session){
		this.sessionList.remove(session);
	}

	public Session newSession(ArrayList<User> participants){
		Session session = new Session(participants);
		this.sessionList.add(session);
		return session;
	}

	public boolean isOnline(){
		return this.isOnline;
	}

	public void setOnline(boolean online){
		this.isOnline=online;
	}
}