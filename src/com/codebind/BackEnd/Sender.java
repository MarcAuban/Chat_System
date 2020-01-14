package com.codebind.BackEnd;

import com.codebind.BackEnd.ChatSystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
* Permet d'envoyer des messages en unicast ou multicast
*/
public class Sender {
	protected static void send(String ip, String message){
		try{
			System.out.println("\n----send----\n" + message + "\n-----end-----");
			DatagramSocket socket = new DatagramSocket();
			InetAddress address = InetAddress.getByName(ip);
			byte[] buf = message.getBytes();

			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 6969);
			socket.send(packet);
			socket.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	protected static void broadcast(String message) {
		try {
			System.out.println("\n--broadcast--\n" + message + "\n-----end-----");
			DatagramSocket socket = new DatagramSocket();
			InetAddress address = InetAddress.getByName(ChatSystem.broadcastIP);
			byte[] buf = message.getBytes();

			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 6969);
			socket.send(packet);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}