package com.codebind;


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
	protected static void save(User user){
		// "_" pour gitignore
		Path path = Paths.get("savefiles","_" + user.getPseudo());
		try {
			//si le savefile existe
			if(new File(path.toString()).exists()) {
				Files.delete(path);
			}
			//créé le fichier
			//nb sessions
			Files.write(path, (user.getSessionList().size() + "\n").getBytes(), StandardOpenOption.CREATE);
			for (Session s : user.getSessionList()) {
				//nb participants
				Files.write(path, (s.getParticipants().size() + "\n").getBytes(), StandardOpenOption.APPEND);
				for (User participant : s.getParticipants()) {
					//participants
					Files.write(path, (participant.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
				}
				//nb messages
				Files.write(path, (s.getHistorique().size() + "\n").getBytes(), StandardOpenOption.APPEND);
				for (Message m : s.getHistorique()) {
					//message
					//m.getSender() + "\n" + m.getMessage() + "\n" + m.getDate() + "\n"
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
	 * @return l'utilisatuer avec ses sessions et leur historique remplies
	 */
	protected static User getUserFromSave(User user){
		// "_" pour gitignore
		Path path = Paths.get("savefiles","_" + user.getPseudo());
		try {
			//si le savefile existe
			if(new File(path.toString()).exists()) {
				List<String> file = Files.readAllLines(path);
				ListIterator<String> line = file.listIterator();

				//recuperer l'historique que si y'a déjà un savefile qui existe
				if (line.hasNext()) {
					int nbSessions = Integer.parseInt(line.next());
					for (int i = 0; i < nbSessions; i++) {

						ArrayList<User> participants = new ArrayList<>();

						int nbParticipants = Integer.parseInt(line.next());
						for (int j = 0; j < nbParticipants; j++) {
							String pseudo = line.next();
							String ip = line.next();
							participants.add(new User(pseudo, ip));
						}

						ArrayList<Message> historique = new ArrayList<>();

						int nbMessages = Integer.parseInt(line.next());
						for (int h = 0; h < nbMessages; h++) {
							//							pseudo		message		date
							String pseudo = line.next();
							String message = line.next();
							String heure = line.next();
							DateFormat format = new SimpleDateFormat("HH:mm");
							//System.out.println("heure : " + heure);
							Date date = format.parse(heure);
							historique.add(new Message(new User(pseudo, ""), message, date));
						}

						Session s = user.newSession(participants);
						s.setHistorique(historique);
					}
				}
			}
		}
		catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return user;
	}
}