package com.codebind.BackEnd;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * sauvegarde de l'historique dans le dossier sauvegarde,
 * à la racine de l'executable
 */
public class Savefile {

	/**
	 * sauvegarde l'historique dans le fichier _pseudo
	 * @param user l'utilisateur qui est en train de se déconnecter
	 */
	protected static void save(User user, ArrayList<User> userList){
		// "_" pour gitignore
		Path path = Paths.get("savefiles","_" + user.getPseudo());
		Path directory = Paths.get("savefiles");
		try {

			//créé le dossier si il existe pas
			File savefiles = new File(directory.toString());
			if(!savefiles.exists()){
				if(!savefiles.mkdir()){
					throw new IOException();
				}
			}

			//supprime l'ancienne save
			if(new File(path.toString()).exists()) {
				Files.delete(path);
			}

			//créé le fichier

			//USERLIST
			Files.write(path, (userList.size() + "\n").getBytes(), StandardOpenOption.CREATE);
			for(User u : userList) {
				Files.write(path, (u.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
			}

			//SESSIONS
			Files.write(path, (user.getSessionList().size() + "\n").getBytes(), StandardOpenOption.APPEND);
			for (Session s : user.getSessionList()) {

				//PARTICIPANTS
				Files.write(path, (s.getParticipants().size() + "\n").getBytes(), StandardOpenOption.APPEND);
				for (User participant : s.getParticipants()) {
					Files.write(path, (participant.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
				}

				//MESSAGES
				Files.write(path, (s.getHistorique().size() + "\n").getBytes(), StandardOpenOption.APPEND);
				for (Message m : s.getHistorique()) {
					Files.write(path, (m.toString()).getBytes(), StandardOpenOption.APPEND);
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * recupère l'historique si une sauvegarde existe
	 * @param user un utilisateur qui veut se connecter
	 */
	protected static void getUserFromSave(User user, ArrayList<User> userList){
		// "_" pour gitignore
		Path path = Paths.get("savefiles","_" + user.getPseudo());

		/*
		System.out.println("\nSavefile avant");
		for(User u : userList)
			System.out.println(u.toString());
		*/

		try {

			//si le savefile existe
			if(new File(path.toString()).exists()) {
				List<String> file = Files.readAllLines(path);
				ListIterator<String> line = file.listIterator();

				//recuperer l'historique que si y'a déjà un savefile qui existe
				if (line.hasNext()) {

					//USERLIST
					int nbUsers = Integer.parseInt(line.next());
					for (int i = 0; i < nbUsers; i++) {
						String pseudo = line.next();
						String ip = line.next();

						if(getUserFromPseudo(userList, pseudo, ip)==null){//il pas dans la liste
							User u = new User(pseudo, ip);
							u.setOnline(false);
							userList.add(u);
						}
					}

					//SESSIONS
					int nbSessions = Integer.parseInt(line.next());
					for (int i = 0; i < nbSessions; i++) {

						//PARTICIPANTS
						ArrayList<User> participants = new ArrayList<>();

						int nbParticipants = Integer.parseInt(line.next());
						for (int j = 0; j < nbParticipants; j++) {
							String pseudo = line.next();
							String ip = line.next();

							User participant = getUserFromPseudo(userList, pseudo, ip);
							if(participant==null){//si il se trouve pas dans userList c'est qu'il est déco
								participant = new User(pseudo, ip);
								participant.setOnline(false);
							}
							participants.add(participant);
						}

						//MESSAGES
						ArrayList<Message> historique = new ArrayList<>();

						int nbMessages = Integer.parseInt(line.next());
						for (int h = 0; h < nbMessages; h++) {
							String pseudo = line.next();
							String message = line.next();
							String heure = line.next();
							DateFormat format = new SimpleDateFormat("HH:mm");
							Date date = format.parse(heure);
							historique.add(new Message(new User(pseudo, ""), message, date));
						}

						//AJOUT DE LA SESSION
						Session s = user.newSession(participants);
						s.setHistorique(historique);
					}
				}
			}

			/*
			System.out.println("\nSavefile après");
			for(User u : userList)
				System.out.println(u.toString());
			*/
		}
		catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	protected static User getUserFromPseudo(ArrayList<User> userList, String pseudo, String ip){
		for(User user : userList) {
			if (user.getPseudo().equals(pseudo) && user.getIP().equals(ip)) {
				return user;
			}
		}
		return null;
	}
}
