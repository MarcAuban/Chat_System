package com.codebind.BackEnd;

import java.net.*;
import java.util.ArrayList;
import java.lang.Thread;
//getMyIP
import java.util.Enumeration;
//queue
import java.lang.InterruptedException;
import java.util.List;
import java.util.concurrent.*;
//flush()


/**
* C'est l'application principale, elle permet de se login,
* elle initialise le receiver et traite les messages reçus.
*/
public class ChatSystem implements Runnable{

	private ArrayList<User> userList = new ArrayList<>();
	private User user=null;
/**
	* l'ip locale de la machine
	*/
	protected static String myIP;
	protected static String broadcastIP;

	private Receiver receiver;
	private BlockingQueue<String> queue;
	private boolean online = true;

/**
	* initialise myIP, le receiverThread et récupère la liste des utilisateurs connectés
	* il faut utiliser la méthode start() pour commencer à utiliser le ChatSystem
	* @param receiver une instance de receiverThread initialisée avec une SynchronousQueue
	* @param queue la même instance de queue que celle fournie à ReceiverThread
	*/
public ChatSystem(Receiver receiver, BlockingQueue<String> queue){
		this.queue=queue;
		getIP();
		this.receiver=receiver;
		Thread receiverThread = new Thread(this.receiver);
		receiverThread.start();
		this.requestUserList();
	}

	@Override
	public void run(){
		try{
			while(this.online){
				received(this.queue.take());
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}

/**
	* à utiliser après login
	*/
	public void deconnexion(){
		this.online = false;
		receiver.deconnexion();
		if(!(this.user == null)){
			Sender.broadcast("deconnexion\n" + this.user.toString());
			Savefile.save(this.user, this.userList);
		}
		Sender.send("127.0.0.1", "débloque le receive");
	}

/**
	* permet à l'utilisateur de se connecter,
	* à faire avant tout
	* @param pseudo le pseudo de l'utilisateur
	*/
	public void login(String pseudo){

		//test si le pseudo est déjà utilisé sur le reseau
		if(!isUnique(pseudo)){
			//UI Stuff
			System.out.println("pseudo déjà pris");
		}
		else{

			//a faire : charger l'user et son historique si il existe dejà

			//création d'un nouveau user
			this.user = new User(pseudo, myIP);
			this.addConnectedUser(this.user);
			Savefile.getUserFromSave(this.user, this.userList);
			this.notifyConnected();
		}
	}

	protected static void getIP(){
		//String ip=null;
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp())
					continue;

				List<InterfaceAddress> addresses = iface.getInterfaceAddresses();
				myIP = addresses.get(0).getAddress().getHostAddress();
				for(InterfaceAddress address : addresses) {
					InetAddress broadcast = address.getBroadcast();
					if(broadcast!=null)
						broadcastIP = broadcast.getHostAddress();
					System.out.println(broadcastIP);
				}
			}
		}
		catch (SocketException e) {
			throw new RuntimeException(e);
		}
	}

/**
	* @return l'utilisateur si il s'est login, null sinon
	*/
	public User getUser(){
		return this.user;
	}

	/**
	 * @return la liste des utilisateurs connectés
	 */
	public ArrayList<User> getUsersConnected(){
		ArrayList<User> usersConnected = new ArrayList<>();
		for(User user : this.userList)
			if(user.isOnline())
				usersConnected.add(user);
		return usersConnected;
	}

	public void requestUserList(){
		Sender.broadcast("getUserList\n" + myIP);
	}

	public User getUserFromPseudo(String pseudo){
		for(User user : this.userList) {
			//System.out.println(user.getPseudo() + " " + pseudo);
			if (user.getPseudo().equals(pseudo)) {
				return user;
			}
		}
		return null;
	}

	public User getUserFromPseudo(String pseudo, String ip){
		for(User user : this.userList) {
			if (user.getPseudo().equals(pseudo) && user.getIP().equals(ip)) {
				return user;
			}
		}
		return null;
	}

/**
	* @param newPseudo le nouveau pseudo de l'utilisateur
	*/
	public boolean changerPseudo(String newPseudo){
		boolean unicite = isUnique(newPseudo);
		if(unicite){
			Sender.broadcast("changerPseudo\n" + this.user.toString() + "\n" + newPseudo);
			this.user.setPseudo(newPseudo);
		}
		else{
			System.out.println("impossible de changer de pseudo");
		}
		return unicite;
	}

	private void changerPseudo(String ancienPseudo, String newPseudo){
		for(User user : this.userList){
			if(user.getPseudo().equals(ancienPseudo)){
				user.setPseudo(newPseudo);
			}
		}
	}

	private boolean isUnique(String pseudo){

		boolean unicite=true;

		for(User user : userList){
			System.out.println("unicité :" + pseudo + " " + user.getPseudo());
			if (pseudo.equals(user.getPseudo())) {
				//unicité pas valide
				unicite = false;
				break;
			}
		}
		return unicite;
	}

	private void notifyConnected(){
		if(this.user!=null)//evite de repondre si l'user est pas login
			Sender.broadcast("connected\n" + this.user.toString());
	}

	private void notifyConnected(String ip){
		if(this.user!=null)//evite de repondre si l'user est pas login
			Sender.send(ip, "connected\n" + this.user.toString());
	}

	private void printUserList(){
		System.out.println("\nusers connectés :");
		System.out.flush();
		for(User user : this.userList){
			System.out.println(user.toString());
			System.out.flush();
		}
		System.out.println();
		System.out.flush();
	}

	/*--------------------
	 à changer en private
	--------------------*/
	protected void addConnectedUser(User user){
		if(isUnique(user.getPseudo())){
			this.userList.add(user);
			this.printUserList();
		}
	}

	private void received(String received){

		System.out.println("\n----recu----");
		System.out.println(received);
		System.out.println("-----end-----\n");
		System.out.flush();

		String[] splitReceived = received.split("\n");


		if(splitReceived[0].equals("getUserList")){
			/*
			"getUserList
			192.0.0.2 -> splitReceived[1]
			"
			*/
			this.notifyConnected(splitReceived[1]);
		}

		if(splitReceived[0].equals("connected")){
			/*
			"connected
			jean marc
			192.34.2.1"
			*/
			//splitReceived[1] est le pseudo et splitReceived[2] est l'ip de l'utilisateur
			System.out.println("\nconnexion");
			this.printUserList();
			User connectedUser = getUserFromPseudo(splitReceived[1], splitReceived[2]);
			if(connectedUser==null) {
				System.out.println("nouveau user");
				this.addConnectedUser(new User(splitReceived[1], splitReceived[2]));
			}
			else//on l'a déjà enregistré
				connectedUser.setOnline(true);
		}

		if(splitReceived[0].equals("changerPseudo")){
			/*
			"changerPseudo	0
			jean marc		1
			192.34.2.1		2
			jean moriss"	3
			*/
			//splitReceived[1] est le pseudo et splitReceived[2] est l'ip de l'utilisateur
			this.changerPseudo(splitReceived[1], splitReceived[3]);
			for(Session s : this.user.getSessionList()){
				s.setDisplayed(false);
			}
		}

		if(splitReceived[0].equals("deconnexion")){
			/*
			"deconnexion
			jean marc
			192.34.2.1
			*/
			getUserFromPseudo(splitReceived[1]).setOnline(false);
			this.printUserList();
		}

		if(splitReceived[0].equals("message")){
			/*
			"message	0
			jean marc	1
			192.34.2.1	2
			2			3
			jean marc	4
			192.34.2.1	5
			jori		6
			192.34.2.2	7
			end			8
			bla"		9
			*/
			User sender = getUserFromPseudo(splitReceived[1]);
			ArrayList<User> participants = new ArrayList<>();
			for(int i=4 ;i<Integer.parseInt(splitReceived[3])*2+4; i+=2){
				//splitReceived[2] est le pseudo et splitReceived[3] est l'ip de l'utilisateur
				participants.add(getUserFromPseudo(splitReceived[i]));
			}
			Session session = this.user.getSessionFromParticipants(participants);
			if(session == null){
				System.out.println("nouvelle session");
				session = this.user.newSession(participants);
			}
			session.receivedMsg(sender, received.substring(received.indexOf("end")+4));
		}
	}
}