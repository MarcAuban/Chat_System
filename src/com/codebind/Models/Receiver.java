package com.codebind.Models;

//queue

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

//network

/**
 * C'est le thread qui recoit les messages,
 * il est automatiquement démarré dans le ChatSystemgity
 */
public class Receiver implements Runnable{

	private DatagramSocket socket = null;
	private boolean online = true;

	private BlockingQueue<String> queue;

	/**
	 * @param queue la même instance de queue que celle fournie à ChatSystem
	 */
	public Receiver(BlockingQueue<String> queue){
		this.queue=queue;
		try{
			this.socket = new DatagramSocket(6969);
		}
		catch(SocketException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		while(this.online){
			byte[] buffer= new byte[60000];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try{
				this.socket.receive(packet);
				byte[] data = new byte[packet.getLength()];
				System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
				String message = new String(data);

				String sender = packet.getAddress().getHostAddress();
				//System.out.println("Sender : " + sender);
				if(!sender.equals(ChatSystem.myIP) && !sender.equals("127.0.0.1")){//eviter le loopback
					try{
						queue.put(message);
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
			catch(IOException e1){
				e1.printStackTrace();
			}
		}
		socket.close();
		try{
			queue.put("débloquer app");
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	protected void deconnexion(){
		this.online = false;
	}
}